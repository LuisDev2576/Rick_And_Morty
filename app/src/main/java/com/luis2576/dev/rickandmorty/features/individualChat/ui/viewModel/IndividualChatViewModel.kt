package com.luis2576.dev.rickandmorty.features.individualChat.ui.viewModel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luis2576.dev.rickandmorty.features.individualChat.domain.model.Contact
import com.luis2576.dev.rickandmorty.features.individualChat.domain.repository.IndividualChatRepository
import com.luis2576.dev.rickandmorty.features.individualChat.domain.useCase.GetContactDetailsUseCase
import com.luis2576.dev.rickandmorty.features.individualChat.ui.state.LoadContactResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class IndividualChatViewModel @Inject constructor(
    private val getContactDetailsUseCase: GetContactDetailsUseCase,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _contact = MutableStateFlow<LoadContactResponse>(LoadContactResponse.Void)
    val contact: StateFlow<LoadContactResponse> = _contact.asStateFlow()

    init {
        val initialContactId: String? = savedStateHandle["contactId"]
        initialContactId?.let { loadContact(it) }
    }

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
}



sealed class DownloadMessagesResponse<out T> {

    // Estado vacío inicial
    object Void : DownloadMessagesResponse<Nothing>()

    // Estado cuando los mensajes se están descargando
    object DownloadingMessages : DownloadMessagesResponse<Nothing>()

    // Estado cuando los mensajes han sido descargados correctamente
    data class MessagesDownloaded<out T>(
        val data: T?
    ) : DownloadMessagesResponse<T>()

    // Estado cuando ocurre un error durante la descarga
    data class Failure(
        val e: Exception
    ) : DownloadMessagesResponse<Nothing>()
}
