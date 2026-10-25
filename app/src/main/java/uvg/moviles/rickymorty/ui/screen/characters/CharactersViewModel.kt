package uvg.moviles.rickymorty.ui.screen.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uvg.moviles.rickymorty.data.local.CharacterDao
import uvg.moviles.rickymorty.data.local.toModel
import uvg.moviles.rickymorty.data.model.Character

data class CharactersUiState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false,
    val showFavoritesOnly: Boolean = false
)

class CharactersViewModel(private val characterDao: CharacterDao) : ViewModel() {
    private val showFavoritesOnly = MutableStateFlow(false)

    val uiState: StateFlow<CharactersUiState> = characterDao.observeAll()
        .map { entities -> entities.map { it.toModel() } }
        .combine(showFavoritesOnly) { characters, favoritesOnly ->
            CharactersUiState(
                isLoading = false,
                data = if (favoritesOnly) characters.filter { it.isFavorite } else characters,
                showFavoritesOnly = favoritesOnly
            )
        }
        .catch { emit(CharactersUiState(isLoading = false, hasError = true)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CharactersUiState())

    fun toggleFavoritesFilter() {
        showFavoritesOnly.value = !showFavoritesOnly.value
    }

    fun toggleFavorite(character: Character) {
        viewModelScope.launch {
            characterDao.updateFavorite(character.id, !character.isFavorite)
        }
    }

    companion object {
        fun factory(characterDao: CharacterDao): ViewModelProvider.Factory = viewModelFactory {
            initializer { CharactersViewModel(characterDao) }
        }
    }
}
