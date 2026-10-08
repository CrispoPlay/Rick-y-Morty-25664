package uvg.moviles.rickymorty.ui.screen.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uvg.moviles.rickymorty.data.CharacterDb
import uvg.moviles.rickymorty.data.model.Character

data class CharactersUiState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false
)

class CharactersViewModel : ViewModel() {
    private val characterDb = CharacterDb()
    private val _uiState = MutableStateFlow(CharactersUiState())
    val uiState: StateFlow<CharactersUiState> = _uiState.asStateFlow()
    private var loadingJob: Job? = null

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        loadingJob?.cancel()
        _uiState.value = CharactersUiState(isLoading = true)
        loadingJob = viewModelScope.launch {
            delay(4_000)
            _uiState.value = CharactersUiState(
                isLoading = false,
                data = characterDb.getAllCharacters()
            )
        }
    }

    fun showError() {
        if (!_uiState.value.isLoading) return
        loadingJob?.cancel()
        _uiState.value = CharactersUiState(isLoading = false, hasError = true)
    }
}
