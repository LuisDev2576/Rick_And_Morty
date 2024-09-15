package com.luis2576.dev.rickandmorty.before.features.individualChat.ui.viewModel

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
import com.luis2576.dev.rickandmorty.before.features.individualChat.domain.model.Contact
import com.luis2576.dev.rickandmorty.before.features.individualChat.domain.model.Conversation
import com.luis2576.dev.rickandmorty.before.features.individualChat.domain.model.Message
import com.luis2576.dev.rickandmorty.before.features.individualChat.domain.useCase.DownloadConversationUseCase
import com.luis2576.dev.rickandmorty.before.features.individualChat.domain.useCase.GetContactDetailsUseCase
import com.luis2576.dev.rickandmorty.before.features.individualChat.domain.useCase.SendMessageUseCase
import com.luis2576.dev.rickandmorty.before.features.individualChat.ui.state.DownloadConversationResponse
import com.luis2576.dev.rickandmorty.before.features.individualChat.ui.state.LoadContactResponse
import com.luis2576.dev.rickandmorty.before.features.individualChat.ui.state.SendMessageResult
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
    fun onEmailChange(message: String) {
        _message = message
    }

    fun sendMessage(userId: String, contact: Contact, conversation: Conversation, newMessage: Message) {
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
                            callVertexAi(userId = userId, contact = contact)
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
    private fun callVertexAi(userId: String, contact: Contact) {
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
                                    Firebase.vertexAI.generativeModel("gemini-1.5-pro")
                                val personality = mutableStateOf(downloadedConversation.contactPersonality)
                                if(downloadedConversation.messages.size == 1){
                                    personality.value = generativeModel.generateContent("Teniendo en cuenta que tu respuesta será tomada para establecer la personalidad o tono de respuesta " +
                                            "de un contacto en una aplicación de chat, genera una personalidad basada en el siguiente personaje de la serie rick y morty, " +
                                            "debes describir también la forma en la que debe responder a mensajes, su humor, sarcasmo, pasiencia, inteligencia, amabilidad, etc, " +
                                            "acá te paso la información del personaje: $contact").text?: "Error en la definición de la personalidad del personaje"
                                }
                                // El mensaje que se enviará como prompt a la IA
                                val prompt = lastMessage.text
                                Log.d("IndividualChatViewModel", "callVertexAi: Prompt enviado a Vertex AI: $prompt")

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
                                            "Responde al siguiente prompt: $prompt, " +
                                                    "teniendo en cuenta la siguiente personalidad: ${personality.value}, " +
                                                    "ten en cuenta que tu te llamas ${contact.name}," +
                                                    " siempre responde de manera coherente con el historial de la conversación y" +
                                                    "No describas comportamientos del contacto o personaje por ejemplo *Resopla* o *Toma una cerveza*, " +
                                                    "limita la respuesta a solo texto porque es para un chat, no un guion," +
                                                    "si te pido que recuerdes algo, me refiero solo al contexto de la conversacion, no mas allá, " +
                                                    "por cierto, no sabes mi nombre, así que no me llames de ninguna manera a menos de que te lo diga"
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
                                sendMessage(userId, contact, downloadedConversation.copy(contactPersonality = personality.value), aiMessage)

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
