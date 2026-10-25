package uvg.moviles.rickymorty.ui.screen.characters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import uvg.moviles.rickymorty.data.local.CharacterDao
import uvg.moviles.rickymorty.data.model.Character
import uvg.moviles.rickymorty.ui.components.ErrorContent
import uvg.moviles.rickymorty.ui.components.LoadingContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen(
    characterDao: CharacterDao,
    onCharacterClick: (Int) -> Unit,
    viewModel: CharactersViewModel = viewModel(factory = CharactersViewModel.factory(characterDao))
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Characters") },
                actions = {
                    FilterChip(
                        selected = uiState.showFavoritesOnly,
                        onClick = viewModel::toggleFavoritesFilter,
                        label = { Text("Favoritos") },
                        leadingIcon = {
                            Icon(
                                if (uiState.showFavoritesOnly) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            )
        }
    ) { innerPadding ->
        when {
            uiState.hasError -> ErrorContent(
                message = "Error al obtener listado de personajes.",
                onRetry = {},
                modifier = Modifier.padding(innerPadding)
            )
            uiState.isLoading -> LoadingContent(
                onClick = {},
                modifier = Modifier.padding(innerPadding)
            )
            uiState.data.isEmpty() -> Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (uiState.showFavoritesOnly) "No hay favoritos para mostrar."
                    else "No hay elementos para mostrar."
                )
            }
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(uiState.data, key = { it.id }) { character ->
                    CharacterRow(
                        character = character,
                        onClick = { onCharacterClick(character.id) },
                        onFavoriteClick = { viewModel.toggleFavorite(character) }
                    )
                }
            }
        }
    }
}
@Composable
private fun CharacterRow(
    character: Character,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = "Imagen de ${character.name}",
                modifier = Modifier.size(72.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(start = 16.dp).weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(character.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    "${character.species} · ${character.status}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(character.gender, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    if (character.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (character.isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
                    tint = if (character.isFavorite) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    }
}
