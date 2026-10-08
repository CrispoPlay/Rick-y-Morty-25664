package uvg.moviles.rickymorty.ui.screen.characters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.navigation.toRoute
import uvg.moviles.rickymorty.data.CharacterDb
import uvg.moviles.rickymorty.data.model.Character
import uvg.moviles.rickymorty.navigation.CharacterDetailsDestination

data class CharacterDetailsUiState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false
)

class CharacterDetailsViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val characterId = savedStateHandle.toRoute<CharacterDetailsDestination>().characterId
    private val characterDb = CharacterDb()
    private val _uiState = MutableStateFlow(CharacterDetailsUiState())
    val uiState: StateFlow<CharacterDetailsUiState> = _uiState.asStateFlow()
    private var loadingJob: Job? = null

    init {
        loadCharacter()
    }

    fun loadCharacter() {
        loadingJob?.cancel()
        _uiState.value = CharacterDetailsUiState(isLoading = true)
        loadingJob = viewModelScope.launch {
            delay(2_000)
            _uiState.value = CharacterDetailsUiState(
                isLoading = false,
                data = characterDb.getCharacterById(characterId)
            )
        }
    }

    fun showError() {
        if (!_uiState.value.isLoading) return
        loadingJob?.cancel()
        _uiState.value = CharacterDetailsUiState(isLoading = false, hasError = true)
    }
}
