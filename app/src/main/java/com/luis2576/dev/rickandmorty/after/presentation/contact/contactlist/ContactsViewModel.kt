package com.luis2576.dev.rickandmorty.after.presentation.contact.contactlist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luis2576.dev.rickandmorty.R
import com.luis2576.dev.rickandmorty.after.domain.models.responses.ContactListState
import com.luis2576.dev.rickandmorty.after.domain.usecases.GetAllContactsPreviewUseCase
import com.luis2576.dev.rickandmorty.after.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona el estado de la lista de contactos.
 *
 * @param getContactsUseCase El caso de uso para obtener los contactos.
 * @param context El contexto de la aplicación
 */
@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val getContactsUseCase: GetAllContactsPreviewUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    /**
     * Flujo de estado mutable que representa el estado actual de la lista de contactos
     */
    private val _contactsListState = MutableStateFlow<ContactListState>(ContactListState.Loading)

    /**
     * Flujo de estado inmutable que expone el estado de la lista de contactos a la UI
     */
    val contactsListState: StateFlow<ContactListState> = _contactsListState.asStateFlow()

    init {
        getContacts(false)
    }

    /**
     * Obtiene los contactos, ya sea desde la caché local o desde el servidor remoto.
     *
     * @param forceFetchFromRemote Indica si se debe forzar la obtención de datos desde el servidor remoto,
     * incluso si hay datos locales disponibles
     */
    private fun getContacts(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            _contactsListState.value = ContactListState.Loading
            getContactsUseCase(forceFetchFromRemote).collect { result ->
                _contactsListState.value = when (result) {
                    is Resource.Error -> ContactListState.Error(result.message ?: context.getString(R.string.unknown_error))
                    is Resource.Success -> ContactListState.Success(result.data.orEmpty())
                    is Resource.Loading -> ContactListState.Loading
                }
            }
        }
    }
}

