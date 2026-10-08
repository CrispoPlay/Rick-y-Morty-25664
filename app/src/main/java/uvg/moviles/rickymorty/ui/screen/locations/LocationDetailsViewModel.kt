package uvg.moviles.rickymorty.ui.screen.locations

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
import uvg.moviles.rickymorty.data.LocationDb
import uvg.moviles.rickymorty.data.model.Location
import uvg.moviles.rickymorty.navigation.LocationDetailsDestination

data class LocationDetailsUiState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false
)

class LocationDetailsViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val locationId = savedStateHandle.toRoute<LocationDetailsDestination>().locationId
    private val locationDb = LocationDb()
    private val _uiState = MutableStateFlow(LocationDetailsUiState())
    val uiState: StateFlow<LocationDetailsUiState> = _uiState.asStateFlow()
    private var loadingJob: Job? = null

    init {
        loadLocation()
    }

    fun loadLocation() {
        loadingJob?.cancel()
        _uiState.value = LocationDetailsUiState(isLoading = true)
        loadingJob = viewModelScope.launch {
            delay(2_000)
            _uiState.value = LocationDetailsUiState(
                isLoading = false,
                data = locationDb.getLocationById(locationId)
            )
        }
    }

    fun showError() {
        if (!_uiState.value.isLoading) return
        loadingJob?.cancel()
        _uiState.value = LocationDetailsUiState(isLoading = false, hasError = true)
    }
}
