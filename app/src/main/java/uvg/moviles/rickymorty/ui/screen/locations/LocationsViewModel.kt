package uvg.moviles.rickymorty.ui.screen.locations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import uvg.moviles.rickymorty.data.local.LocationDao
import uvg.moviles.rickymorty.data.local.toModel
import uvg.moviles.rickymorty.data.model.Location

data class LocationsUiState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false
)

class LocationsViewModel(locationDao: LocationDao) : ViewModel() {
    val uiState: StateFlow<LocationsUiState> = locationDao.observeAll()
        .map { entities -> LocationsUiState(isLoading = false, data = entities.map { it.toModel() }) }
        .catch { emit(LocationsUiState(isLoading = false, hasError = true)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LocationsUiState())

    companion object {
        fun factory(locationDao: LocationDao): ViewModelProvider.Factory = viewModelFactory {
            initializer { LocationsViewModel(locationDao) }
        }
    }
}
