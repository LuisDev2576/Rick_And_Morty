package com.luis2576.dev.rickandmorty.after.presentation.chats.individualchats

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.vertexai.type.content
import com.google.firebase.vertexai.vertexAI
import com.luis2576.dev.rickandmorty.after.domain.models.Contact
import com.luis2576.dev.rickandmorty.after.domain.models.Conversation
import com.luis2576.dev.rickandmorty.after.domain.models.Message
import com.luis2576.dev.rickandmorty.after.domain.models.responses.DownloadConversationResponse
import com.luis2576.dev.rickandmorty.after.domain.models.responses.LoadContactResponse
import com.luis2576.dev.rickandmorty.after.domain.models.responses.SendMessageResult
import com.luis2576.dev.rickandmorty.after.domain.usecases.DownloadConversationUseCase
import com.luis2576.dev.rickandmorty.after.domain.usecases.GetContactDetailsUseCase
import com.luis2576.dev.rickandmorty.after.domain.usecases.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class IndividualChatViewModel @Inject constructor(
    private val getContactDetailsUseCase: GetContactDetailsUseCase,
    private val downloadConversationUseCase: DownloadConversationUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _contact = MutableStateFlow<LoadContactResponse>(LoadContactResponse.Void)
    val contact: StateFlow<LoadContactResponse> = _contact.asStateFlow()

    // Estado para manejar la conversación descargada
    private var _downloadedConversation = MutableStateFlow<DownloadConversationResponse>(
        DownloadConversationResponse.Void)
    val downloadedConversation: StateFlow<DownloadConversationResponse>  = _downloadedConversation

    // Estado para manejar el resultado del envío del mensaje
    private var _sendMessageResult = MutableStateFlow<SendMessageResult>(SendMessageResult.Void)
    val sendMessageResult: StateFlow<SendMessageResult> get() = _sendMessageResult

    fun loadContact(contactId: String) {
        viewModelScope.launch {
            _contact.value = LoadContactResponse.LoadingContact
            try {
                val contact = getContactDetailsUseCase(contactId)
                _contact.value = LoadContactResponse.ContactLoaded(contact)
            } catch (e: Exception) {
                _contact.value = LoadContactResponse.Failure(e.toString())
            }
        }
    }

    fun downloadConversation(contactId: String, userId: String) {
        viewModelScope.launch {
            Log.d("IndividualChatViewModel", "downloadConversation: Iniciando descarga de conversación para contactId=$contactId, userId=$userId")

            downloadConversationUseCase.invoke(contactId, userId).collect { response ->
                Log.d("IndividualChatViewModel", "downloadConversation: Recibiendo respuesta de descarga")

                _downloadedConversation.value = response

                when (response) {
                    is DownloadConversationResponse.DownloadingConversation -> {
                        Log.d("IndividualChatViewModel", "downloadConversation: Conversación en proceso de descarga")
                    }
                    is DownloadConversationResponse.MessagesDownloaded -> {
                        Log.d("IndividualChatViewModel", "downloadConversation: Conversación descargada exitosamente")
                    }
                    is DownloadConversationResponse.Failure -> {
                        Log.e("IndividualChatViewModel", "downloadConversation: Error al descargar la conversación", response.e)
                    }
                    else -> {
                        Log.w("IndividualChatViewModel", "downloadConversation: Respuesta no esperada")
                    }
                }
            }
        }
    }

    private var _message by mutableStateOf("")
    val message: String get() = _message
    fun onMessage(message: String) {
        _message = message
    }

    fun sendMessage(userId: String, contact: Contact, conversation: Conversation, newMessage: Message, userName: String) {
        viewModelScope.launch {
            if (message.isEmpty() && newMessage.sendByMe) {
                Log.d("IndividualChatViewModel", "sendMessage: Mensaje vacío, no se envía nada para userId=$userId, contactId=${contact.id}")
            } else {
                Log.d("IndividualChatViewModel", "sendMessage: Enviando mensaje para userId=$userId, contactId=${contact.id}")
                _sendMessageResult.value = SendMessageResult.SendingMessage // Indicar que el envío está en progreso

                try {

                    val result = sendMessageUseCase.invoke(
                        userId = userId,
                        contact = contact,
                        conversation = conversation.copy(messages = conversation.messages.plus(newMessage))
                    )
                    if (result.isSuccess) {
                        Log.d("IndividualChatViewModel", "sendMessage: Mensaje enviado exitosamente para userId=$userId, contactId=${contact.id}")
                        if (newMessage.sendByMe) {
                            Log.d("IndividualChatViewModel", "sendMessage: Llamando a Vertex AI con el nuevo mensaje")
                            callVertexAi(userId = userId, contact = contact, userName = userName)
                        }
                        _message = ""
                        _sendMessageResult.value = SendMessageResult.MessageSent(newMessage)
                    } else {
                        Log.e("IndividualChatViewModel", "sendMessage: Error enviando mensaje para userId=$userId, contactId=${contact.id}. Resultado no exitoso.")
                        _sendMessageResult.value = SendMessageResult.Failure(Exception("Error: Resultado no exitoso al enviar mensaje."))
                    }
                } catch (e: Exception) {
                    Log.e("IndividualChatViewModel", "sendMessage: Excepción al enviar mensaje para userId=$userId, contactId=${contact.id}", e)
                    _sendMessageResult.value = SendMessageResult.Failure(e)
                }
            }
        }
    }

    // Función que llama a Vertex AI para generar una respuesta basada en el último mensaje
    private fun callVertexAi(userId: String, contact: Contact, userName: String) {
        viewModelScope.launch {
            downloadedConversation.collect { response ->
                when (response) {
                    is DownloadConversationResponse.MessagesDownloaded -> {
                        val downloadedConversation = response.conversation // Conversación descargada

                        // Verifica que el último mensaje no esté vacío
                        val lastMessage = downloadedConversation.messages.last()

                        if (lastMessage.text.isNullOrEmpty()) {
                            Log.e("IndividualChatViewModel", "callVertexAi: Último mensaje vacío, no se puede generar respuesta de la IA.")
                            return@collect
                        }

                        if(!downloadedConversation.messages.last().sendByMe){
                            return@collect
                        }

                        try {
                            if (lastMessage.sendByMe) {
                                // Inicializa el modelo generativo Gemini 1.5
                                Log.d(
                                    "IndividualChatViewModel",
                                    "callVertexAi: Inicializando modelo generativo Gemini 1.5"
                                )
                                val generativeModel =
                                    Firebase.vertexAI.generativeModel("gemini-1.5-flash")
                                val personality = mutableStateOf(downloadedConversation.contactPersonality)

                                if(downloadedConversation.messages.size == 1){
                                    val prompt = """
                                        **Objetivo:**
                                    
                                        Generar una personalidad detallada y fiel para ${contact.name} de Rick & Morty, que permita mantener conversaciones interesantes, coherentes y en su estilo característico. 
                                    
                                        **Información del personaje:**
                                    
                                        * **Nombre:** ${contact.name}
                                        * **Estatus:** ${contact.status}
                                        * **Especie:** ${contact.species}
                                        * **Tipo:** ${contact.type}
                                        * **Género:** ${contact.gender}
                                        * **Lugar de origen:** ${contact.originName}
                                        * **Ubicación actual:** ${contact.locationName}
                                        * **Imagen:** ${contact.image}
                                        * **Episodios en los que aparece:** ${contact.episodeList}
                                    
                                        **Personalidad:**
                                    
                                        * **Rasgos principales:** Describe los rasgos de personalidad más destacados del personaje (ej: cínico, inteligente, inseguro, aventurero, etc.).
                                        * **Forma de hablar:** ¿Cómo habla el personaje? (ej: utiliza jerga científica, tartamudea, es sarcástico, grita, etc.).
                                        * **Intereses y motivaciones:** ¿Qué le gusta y qué le motiva al personaje?
                                        * **Relación con otros personajes:** ¿Cómo se relaciona con otros personajes clave de la serie?
                                        * **Debilidades y miedos:** ¿Cuáles son sus puntos débiles y sus miedos?
                                    
                                        **Instrucciones para la conversación:**
                                    
                                        * **Mantener la coherencia:** Las respuestas deben ser coherentes con la personalidad del personaje y su historia en la serie.
                                        * **Responder a las preguntas:** El personaje debe responder directamente a las preguntas del usuario, incluso si lo hace de forma evasiva o sarcástica, según su personalidad.
                                        * **Utilizar su estilo característico:** El personaje debe expresarse utilizando su forma de hablar y vocabulario habituales.
                                        * **Incluir referencias a la serie:** En la medida de lo posible, incluir referencias a eventos o situaciones de la serie para enriquecer la conversación.
                                        * **Ser creativo:** No dudes en improvisar y añadir un toque de humor o sorpresa a las respuestas, siempre que sea coherente con el personaje.
                                    
                                        **Notas adicionales:**
                                    
                                        * Si el personaje tiene frases recurrentes o muletillas, inclúyelas en su forma de hablar.
                                        * Considera el contexto de los episodios en los que aparece el personaje para entender mejor su personalidad y motivaciones.
                                        """
                                    personality.value = generativeModel.generateContent(prompt).text?: "Error en la definición de la personalidad del personaje"
                                }
                                // El mensaje que se enviará como prompt a la IA
                                val userMessage = lastMessage.text
                                Log.d("IndividualChatViewModel", "callVertexAi: Prompt enviado a Vertex AI: $userMessage")

                                // Crear el historial de mensajes para enviar a la IA
                                val chatHistory = downloadedConversation.messages.map {
                                    if (it.sendByMe) {
                                        // El rol "user" para los mensajes del usuario
                                        content(role = "user") { text(it.text?:"")}
                                    } else {
                                        // El rol "model" para los mensajes generados por la IA
                                        content(role = "model") {text(it.text?:"")}
                                    }
                                }

                                // Iniciar el chat con el historial y enviar el nuevo mensaje (prompt)
                                val chat = generativeModel.startChat(history = chatHistory)
                                val response = chat.sendMessage(
                                    content("user"){
                                        text(
                                            """
                                            **Instrucciones:**
                                
                                            Eres ${contact.name} de Rick & Morty. Estás conversando con $userName. 
                                
                                            **Personalidad de ${contact.name}:**
                                
                                            ${personality.value}
                                
                                            **Nuevo mensaje de $userName:**
                                
                                            $userMessage
                                
                                            **Responde al mensaje de $userName siguiendo estas pautas:**
                                
                                            * **Mantén la coherencia con tu personalidad, utilizando tu estilo característico y, si es posible, incluyendo referencias a la serie.**
                                            * **No utilices ningún tipo de elemento o carácter para expresar una acción (por ejemplo, *, -, etc.). Solo responde con texto como en una conversación normal.**
                                            * **Aunque debes darle un toque personal a tus respuestas basado en tu personalidad, siempre responde a lo que se te pide o se te pregunta.**
                                            * **Asegúrate de que tus respuestas cumplan con las políticas de seguridad y de mensajes indebidos de Gemini. No generes contenido que sea dañino, inseguro, sesgado, o que promueva la discriminación o el odio.** 
                                
                                            """
                                        )
                                    }
                                )

                                Log.d("IndividualChatViewModel", "callVertexAi: Respuesta de la IA recibida: ${response.text}")
                                // Crear un nuevo mensaje con la respuesta de la IA
                                val aiMessage = Message(
                                    text = response.text,
                                    sendByMe = false,  // Lo envió la IA
                                    timestamp = System.currentTimeMillis(),
                                    read = false,
                                    imageUrl = null
                                )

                                // Actualizar la conversación con el nuevo mensaje de la IA
                                Log.d("IndividualChatViewModel", "callVertexAi: Conversación actualizada con el nuevo mensaje de la IA.")

                                // Enviar el mensaje de la IA a Firebase
                                sendMessage(userId, contact, downloadedConversation.copy(contactPersonality = personality.value), aiMessage, userName = userName)

                            }

                        } catch (e: Exception) {
                            Log.e("IndividualChatViewModel", "callVertexAi: Error llamando a Vertex AI para userId=$userId, contactId=${contact.id}", e)
                        }
                    }

                    is DownloadConversationResponse.Failure -> {
                        Log.e("IndividualChatViewModel", "callVertexAi: Error al descargar la conversación", response.e)
                    }

                    else -> {
                        Log.w("IndividualChatViewModel", "callVertexAi: Estado inesperado durante la descarga de la conversación.")
                    }
                }
            }
        }
    }

}

