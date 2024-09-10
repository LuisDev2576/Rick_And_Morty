package com.luis2576.dev.rickandmorty.features.individualChat.data.repository

import android.content.Context
import com.luis2576.dev.rickandmorty.R
import com.luis2576.dev.rickandmorty.features.contacts.data.mappers.toContact
import com.luis2576.dev.rickandmorty.features.individualChat.domain.model.Contact
import com.luis2576.dev.rickandmorty.features.individualChat.domain.dataSource.IndividualChatDataSource
import com.luis2576.dev.rickandmorty.features.individualChat.domain.model.Conversation
import com.luis2576.dev.rickandmorty.features.individualChat.domain.repository.IndividualChatRepository
import com.luis2576.dev.rickandmorty.features.individualChat.ui.state.DownloadConversationResponse
import com.luis2576.dev.rickandmorty.util.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//TODO Ajustar doc
class IndividualChatRepositoryImpl
@Inject
constructor(
    private val individualChatDataSource: IndividualChatDataSource,
    @ApplicationContext private val context: Context
) : IndividualChatRepository {

    /**
     * Obtiene los detalles de un contacto por su ID desde la base de datos local
     *
     * @param id el ID del contacto a buscar
     * @return Un objeto `Resource` que contiene el contacto o un error en caso de fallo
     */
    override suspend fun getContactById(id: String): Resource<Contact> {
        return try {
            val contactEntity = individualChatDataSource.getContactById(id)
            Resource.Success(contactEntity.toContact())
        } catch (e: Exception) {
            Resource.Error(context.getString(R.string.failed_to_load_contact_details))
        }
    }
    override fun downloadMessages(userId: String, contactId: String): Flow<DownloadConversationResponse> {
        return individualChatDataSource.downloadMessages(userId, contactId)
    }

    override suspend fun sendMessage(userId: String, contact: Contact, conversation: Conversation): Result<Unit> {
        return try {
            individualChatDataSource.sendMessage(userId, contact, conversation)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
