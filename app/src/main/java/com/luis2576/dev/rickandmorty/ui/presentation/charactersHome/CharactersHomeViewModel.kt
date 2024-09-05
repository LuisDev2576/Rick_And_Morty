package com.luis2576.dev.rickandmorty.ui.presentation.charactersHome

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luis2576.dev.rickandmorty.R
import com.luis2576.dev.rickandmorty.domain.model.MyCharacterPreview
import com.luis2576.dev.rickandmorty.domain.useCase.GetCharactersPreviewUseCase
import com.luis2576.dev.rickandmorty.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CharactersHomeViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersPreviewUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _charactersListState = MutableStateFlow<CharactersListState>(
        CharactersListState.Loading
    )
    val charactersListState: StateFlow<CharactersListState> = _charactersListState.asStateFlow()

    init {
        getCharacters(false)
    }

    fun getCharacters(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            _charactersListState.value = CharactersListState.Loading
            getCharactersUseCase(forceFetchFromRemote).collect { result ->
                _charactersListState.value = when (result) {
                    is Resource.Error -> CharactersListState.Error(
                        result.message ?: context.getString(R.string.unknown_error)
                    )
                    is Resource.Success -> CharactersListState.Success(result.data.orEmpty())
                    is Resource.Loading -> CharactersListState.Loading
                }
            }
        }
    }

    sealed class CharactersListState {
        data object Loading : CharactersListState()
        data class Success(val characters: List<MyCharacterPreview>) : CharactersListState()
        data class Error(val message: String) : CharactersListState()
    }
}
