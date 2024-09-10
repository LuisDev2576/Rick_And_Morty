package com.luis2576.dev.rickandmorty.features.individualChat.data.dataSource

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.luis2576.dev.rickandmorty.features.authentication.domain.model.UserInformation
import com.luis2576.dev.rickandmorty.features.chatsHome.domain.model.ChatPreview
import com.luis2576.dev.rickandmorty.features.contacts.data.local.ContactEntity
import com.luis2576.dev.rickandmorty.features.contacts.data.local.ContactsDao
import com.luis2576.dev.rickandmorty.features.individualChat.domain.dataSource.IndividualChatDataSource
import com.luis2576.dev.rickandmorty.features.individualChat.domain.model.Contact
import com.luis2576.dev.rickandmorty.features.individualChat.domain.model.Conversation
import com.luis2576.dev.rickandmorty.features.individualChat.ui.state.DownloadConversationResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

//TODO Ajustar doc
class IndividualChatDataSourceImpl @Inject constructor(
    private val contactDao: ContactsDao,
    private val firebaseFirestore: FirebaseFirestore
) : IndividualChatDataSource {

    /**
     * Obtiene un contacto por su ID desde la base de datos
     *
     * @param contactId ID del contacto a buscar
     * @return Entidad del contacto si se encuentra, null en caso contrario
     */
    override suspend fun getContactById(contactId: String): ContactEntity {
        return contactDao.getContactById(contactId.toInt())
    }

    override fun downloadMessages(
        contactId: String,
        userId: String
    ): Flow<DownloadConversationResponse> = callbackFlow {
        Log.d("IndividualChatDataSource", "downloadMessages: Iniciando descarga de conversación para userId=$userId, contactId=$contactId")

        trySend(DownloadConversationResponse.DownloadingConversation).isSuccess

        val subscription = firebaseFirestore
            .collection("users")
            .document(userId)
            .collection("chats")
            .document(contactId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("IndividualChatDataSource", "downloadMessages: Error al escuchar el documento", e)
                    trySend(DownloadConversationResponse.Failure(e)).isSuccess
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("IndividualChatDataSource", "downloadMessages: Documento recibido, procesando datos")
                    val conversation = snapshot.toObject(Conversation::class.java)
                    if (conversation != null) {
                        Log.d("IndividualChatDataSource", "downloadMessages: Conversación descargada correctamente")
                        trySend(DownloadConversationResponse.MessagesDownloaded(conversation)).isSuccess
                    } else {
                        Log.w("IndividualChatDataSource", "downloadMessages: Error al mapear la conversación")
                        val error = Exception("Error parsing conversation")
                        trySend(DownloadConversationResponse.Failure(error)).isSuccess
                    }
                } else {
                    Log.w("IndividualChatDataSource", "downloadMessages: Documento no existe")
                    val error = Exception("Conversation document does not exist")
                    trySend(DownloadConversationResponse.Failure(error)).isSuccess
                }
            }

        awaitClose {
            Log.d("IndividualChatDataSource", "downloadMessages: Cerrando el listener de mensajes")
            subscription.remove()
        }
    }
    override suspend fun sendMessage(
        userId: String,
        contact: Contact,
        conversation: Conversation
    ) {
        try {
            Log.d("IndividualChatDataSource", "sendMessage: Iniciando envío del mensaje para userId=$userId, contactId=$contact")

            val lastMessage = conversation.messages.last()
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
            firebaseFirestore
                .collection("users")
                .document(userId)
                .collection("chats")
                .document(contact.id)
                .set(conversation)
                .await()

            // Obtener el documento del usuario para actualizar la lista de chatPreviews
            val userDocument = firebaseFirestore
                .collection("users")
                .document(userId)
                .get()
                .await()

            // Obtener la lista actual de chatPreviews
            val chatPreviews = userDocument.toObject(UserInformation::class.java)?.previewChats?.toMutableList() ?: mutableListOf()

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
    }

}