package com.luis2576.dev.rickandmorty.data.repositories

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.luis2576.dev.rickandmorty.R
import com.luis2576.dev.rickandmorty.data.local.database.ContactsDao
import com.luis2576.dev.rickandmorty.data.local.models.toDomain
import com.luis2576.dev.rickandmorty.domain.models.ChatPreview
import com.luis2576.dev.rickandmorty.domain.models.UserInformation
import com.luis2576.dev.rickandmorty.domain.models.Contact
import com.luis2576.dev.rickandmorty.domain.models.Conversation
import com.luis2576.dev.rickandmorty.domain.models.responses.DownloadConversationResponse
import com.luis2576.dev.rickandmorty.domain.repositories.IndividualChatRepository
import com.luis2576.dev.rickandmorty.util.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

//TODO Ajustar doc
class IndividualChatRepositoryImpl
@Inject
constructor(
    @ApplicationContext private val context: Context,
    private val firebaseFirestore: FirebaseFirestore,
    private val contactDao: ContactsDao
) : IndividualChatRepository {

    /**
     * Obtiene los detalles de un contacto por su ID desde la base de datos local
     *
     * @param id el ID del contacto a buscar
     * @return Un objeto `Resource` que contiene el contacto o un error en caso de fallo
     */
    override suspend fun getContactById(id: String): Resource<Contact> {
        return try {
            val contactEntity = contactDao.getContactById(id.toInt())
            Resource.Success(contactEntity.toDomain())
        } catch (e: Exception) {
            Resource.Error(context.getString(R.string.failed_to_load_contact_details))
        }
    }
    override fun downloadMessages(contactId: String, userId: String): Flow<DownloadConversationResponse>  = callbackFlow {
        val TAG = "IndividualChatDataSource" // Etiqueta para los logs

        Log.d(TAG, "downloadMessages: Iniciando descarga de conversación para userId=$userId, contactId=$contactId")

        trySend(DownloadConversationResponse.DownloadingConversation).isSuccess

        val documentPath = "users/$userId/chats/$contactId" // Ruta completa al documento

        val subscription = firebaseFirestore
            .collection("users")
            .document(userId)
            .collection("chats")
            .document(contactId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e(TAG, "downloadMessages: Error al escuchar el documento $documentPath", e)
                    trySend(DownloadConversationResponse.Failure(e)).isSuccess
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "downloadMessages: Documento $documentPath recibido, procesando datos")
                    val conversation = snapshot.toObject(Conversation::class.java)
                    if (conversation != null) {
                        Log.d(TAG, "downloadMessages: Conversación descargada correctamente desde $documentPath")
                        trySend(DownloadConversationResponse.MessagesDownloaded(conversation)).isSuccess
                    } else {
                        Log.w(TAG, "downloadMessages: Error al mapear la conversación desde $documentPath")
                        val error = Exception("Error parsing conversation from $documentPath")
                        trySend(DownloadConversationResponse.Failure(error)).isSuccess
                    }
                } else {
                    Log.w(TAG, "downloadMessages: Documento $documentPath no existe")
                    val error = Exception("Conversation document $documentPath does not exist")
                    trySend(DownloadConversationResponse.Failure(error)).isSuccess
                }
            }

        awaitClose {
            Log.d(TAG, "downloadMessages: Cerrando el listener de mensajes para $documentPath")
            subscription.remove()
        }
    }

    override suspend fun sendMessage(userId: String, contact: Contact, conversation: Conversation): Result<Unit> {
        return try {

            try {
                Log.d("IndividualChatDataSource", "sendMessage: Iniciando envío del mensaje para userId=$userId, contactId=${contact.id}")

                // Verifica cuántos mensajes hay en la conversación
                Log.d("IndividualChatDataSource", "sendMessage: Número de mensajes en la conversación: ${conversation.messages.size}")

                val lastMessage = conversation.messages.last()
                Log.d("IndividualChatDataSource", "sendMessage: Último mensaje -> text=${lastMessage.text}, timestamp=${lastMessage.timestamp}")

                val chatPreview = ChatPreview(
                    characterId = contact.id,
                    characterName = contact.name,
                    characterImageUrl = contact.image,
                    text = lastMessage.text,
                    imageUrl = lastMessage.imageUrl,
                    timestamp = lastMessage.timestamp,
                    sendByMe = lastMessage.sendByMe,
                    read = lastMessage.read
                )

                // Actualizar la conversación en Firestore
                Log.d("IndividualChatDataSource", "sendMessage: Actualizando la conversación en Firestore")
                firebaseFirestore
                    .collection("users")
                    .document(userId)
                    .collection("chats")
                    .document(contact.id)
                    .set(conversation)
                    .await()
                Log.d("IndividualChatDataSource", "sendMessage: Conversación actualizada correctamente en Firestore")

                // Obtener el documento del usuario
                Log.d("IndividualChatDataSource", "sendMessage: Obteniendo el documento del usuario con userId=$userId")
                val userDocument = firebaseFirestore
                    .collection("users")
                    .document(userId)
                    .get()
                    .await()

                // Obtener la lista actual de chatPreviews
                val chatPreviews = userDocument.toObject(UserInformation::class.java)?.chatPreviews?.toMutableList() ?: mutableListOf()

                // Buscar y reemplazar el preview existente o agregar uno nuevo
                val index = chatPreviews.indexOfFirst { it.characterId == chatPreview.characterId }
                if (index != -1) {
                    // Reemplazar el elemento existente
                    chatPreviews[index] = chatPreview
                } else {
                    // Agregar el nuevo chatPreview
                    chatPreviews.add(chatPreview)
                }

                // Guardar la lista actualizada de chatPreviews en Firestore
                Log.d("IndividualChatDataSource", "sendMessage: Actualizando chatPreviews en Firestore -> $chatPreviews")
                firebaseFirestore
                    .collection("users")
                    .document(userId)
                    .update("chatPreviews", chatPreviews)
                    .await()

                Log.d("IndividualChatDataSource", "sendMessage: Mensaje enviado correctamente y chatPreview actualizado")

            } catch (e: Exception) {
                Log.e("IndividualChatDataSource", "sendMessage: Error al enviar el mensaje", e)
                throw e
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
