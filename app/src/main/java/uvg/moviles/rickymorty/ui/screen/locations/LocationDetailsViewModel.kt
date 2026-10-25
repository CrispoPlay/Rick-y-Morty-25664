package uvg.moviles.rickymorty.ui.screen.locations

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
import uvg.moviles.rickymorty.data.local.LocationDao
import uvg.moviles.rickymorty.data.local.toModel
import uvg.moviles.rickymorty.data.model.Location
import uvg.moviles.rickymorty.navigation.LocationDetailsDestination

data class LocationDetailsUiState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false
)

class LocationDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    locationDao: LocationDao
) : ViewModel() {
    private val locationId = savedStateHandle.toRoute<LocationDetailsDestination>().locationId

    val uiState: StateFlow<LocationDetailsUiState> = locationDao.observeById(locationId)
        .map { LocationDetailsUiState(isLoading = false, data = it?.toModel()) }
        .catch { emit(LocationDetailsUiState(isLoading = false, hasError = true)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LocationDetailsUiState())

    companion object {
        fun factory(locationDao: LocationDao): ViewModelProvider.Factory = viewModelFactory {
            initializer { LocationDetailsViewModel(createSavedStateHandle(), locationDao) }
        }
    }
}
