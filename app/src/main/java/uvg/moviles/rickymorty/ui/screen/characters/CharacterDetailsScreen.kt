package uvg.moviles.rickymorty.ui.screen.characters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import uvg.moviles.rickymorty.data.local.CharacterDao
import uvg.moviles.rickymorty.data.model.Character
import uvg.moviles.rickymorty.ui.components.DetailRow
import uvg.moviles.rickymorty.ui.components.ErrorContent
import uvg.moviles.rickymorty.ui.components.LoadingContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailsScreen(
    characterDao: CharacterDao,
    onBackClick: () -> Unit,
    viewModel: CharacterDetailsViewModel = viewModel(factory = CharacterDetailsViewModel.factory(characterDao))
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Character details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                actions = {
                    uiState.data?.let { character ->
                        IconButton(onClick = viewModel::toggleFavorite) {
                            Icon(
                                if (character.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = if (character.isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
                                tint = if (character.isFavorite) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        when {
            uiState.hasError -> ErrorContent(
                message = "Error al obtener el personaje.",
                onRetry = {},
                modifier = Modifier.padding(innerPadding)
            )
            uiState.isLoading -> LoadingContent(onClick = {}, modifier = Modifier.padding(innerPadding))
            uiState.data == null -> Text(
                "Personaje no encontrado.",
                modifier = Modifier.padding(innerPadding).padding(24.dp)
            )
            else -> CharacterDetailsContent(
                character = requireNotNull(uiState.data),
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
@Composable
private fun CharacterDetailsContent(character: Character, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 28.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = character.image,
            contentDescription = "Imagen de ${character.name}",
            modifier = Modifier.size(220.dp).clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(20.dp))
        Text(
            character.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(32.dp))
        DetailRow("Species", character.species)
        DetailRow("Status", character.status)
        DetailRow("Gender", character.gender)
    }
}
