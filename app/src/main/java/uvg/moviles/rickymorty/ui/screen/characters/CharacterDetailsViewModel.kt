package uvg.moviles.rickymorty.ui.screen.characters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uvg.moviles.rickymorty.data.local.CharacterDao
import uvg.moviles.rickymorty.data.local.toModel
import uvg.moviles.rickymorty.data.model.Character
import uvg.moviles.rickymorty.navigation.CharacterDetailsDestination

data class CharacterDetailsUiState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false
)

class CharacterDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val characterDao: CharacterDao
) : ViewModel() {
    private val characterId = savedStateHandle.toRoute<CharacterDetailsDestination>().characterId

    val uiState: StateFlow<CharacterDetailsUiState> = characterDao.observeById(characterId)
        .map { CharacterDetailsUiState(isLoading = false, data = it?.toModel()) }
        .catch { emit(CharacterDetailsUiState(isLoading = false, hasError = true)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CharacterDetailsUiState())

    fun toggleFavorite() {
        val character = uiState.value.data ?: return
        viewModelScope.launch {
            characterDao.updateFavorite(character.id, !character.isFavorite)
        }
    }

    companion object {
        fun factory(characterDao: CharacterDao): ViewModelProvider.Factory = viewModelFactory {
            initializer { CharacterDetailsViewModel(createSavedStateHandle(), characterDao) }
        }
    }
}
