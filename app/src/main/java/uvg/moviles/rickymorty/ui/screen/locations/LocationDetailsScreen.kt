package uvg.moviles.rickymorty.ui.screen.locations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import uvg.moviles.rickymorty.data.model.Location
import uvg.moviles.rickymorty.ui.components.DetailRow
import uvg.moviles.rickymorty.ui.components.ErrorContent
import uvg.moviles.rickymorty.ui.components.LoadingContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailsScreen(
    onBackClick: () -> Unit,
    viewModel: LocationDetailsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Location details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { innerPadding ->
        when {
            uiState.hasError -> ErrorContent(
                message = "Error al obtener la ubicación.",
                onRetry = viewModel::loadLocation,
                modifier = Modifier.padding(innerPadding)
            )
            uiState.isLoading -> LoadingContent(
                onClick = viewModel::showError,
                modifier = Modifier.padding(innerPadding)
            )
            else -> uiState.data?.let { location ->
                LocationDetailsContent(
                    location = location,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun LocationDetailsContent(location: Location, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 32.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            location.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(40.dp))
        DetailRow("ID", location.id.toString())
        DetailRow("Type", location.type)
        DetailRow("Dimension", location.dimension)
    }
}
