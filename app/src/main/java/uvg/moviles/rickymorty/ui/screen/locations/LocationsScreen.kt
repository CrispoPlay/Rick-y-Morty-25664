package uvg.moviles.rickymorty.ui.screen.locations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import uvg.moviles.rickymorty.data.model.Location
import uvg.moviles.rickymorty.ui.components.ErrorContent
import uvg.moviles.rickymorty.ui.components.LoadingContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen(
    onLocationClick: (Int) -> Unit,
    viewModel: LocationsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("Locations") }) }) { innerPadding ->
        when {
            uiState.hasError -> ErrorContent(
                message = "Error al obtener listado de ubicaciones.",
                onRetry = viewModel::loadLocations,
                modifier = Modifier.padding(innerPadding)
            )
            uiState.isLoading -> LoadingContent(
                onClick = viewModel::showError,
                modifier = Modifier.padding(innerPadding)
            )
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(uiState.data, key = { it.id }) { location ->
                    Column(
                        modifier = Modifier.fillMaxWidth().clickable { onLocationClick(location.id) }
                            .padding(horizontal = 24.dp, vertical = 18.dp)
                    ) {
                        Text(location.name, style = MaterialTheme.typography.titleLarge)
                        Text(
                            location.type,
                            modifier = Modifier.padding(top = 4.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                }
            }
        }
    }
}
