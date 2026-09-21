package uvg.moviles.rickymorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil.compose.AsyncImage
import kotlinx.serialization.Serializable
import uvg.moviles.rickymorty.ui.theme.RickYMortyTheme

@Serializable
data object Login

@Serializable
data object Characters

@Serializable
data class CharacterDetails(
    val characterId: Int
)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RickYMortyTheme {
                RickAndMortyApp(
                    onCloseApp = ::finish
                )
            }
        }
    }
}

@Composable
fun RickAndMortyApp(
    navController: NavHostController = rememberNavController(),
    onCloseApp: () -> Unit
) {
    val characterDb = remember {
        CharacterDb()
    }

    NavHost(
        navController = navController,
        startDestination = Login
    ) {
        composable<Login> {
            LoginScreen(
                onStartClick = {
                    navController.navigate(Characters) {
                        popUpTo<Login> {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            )
        }

        composable<Characters> {
            BackHandler {
                onCloseApp()
            }

            CharactersScreen(
                characters = characterDb.getAllCharacters(),
                onCharacterClick = { characterId ->
                    navController.navigate(
                        CharacterDetails(
                            characterId = characterId
                        )
                    )
                }
            )
        }

        composable<CharacterDetails> { backStackEntry ->
            val destination =
                backStackEntry.toRoute<CharacterDetails>()

            val character =
                characterDb.getCharacterById(
                    destination.characterId
                )

            CharacterDetailsScreen(
                character = character,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
private fun LoginScreen(
    onStartClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 32.dp,
                    vertical = 24.dp
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(
                        id = R.drawable.rick_and_morty_logo
                    ),
                    contentDescription = "Logo de Rick and Morty",
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(150.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(
                    modifier = Modifier.height(32.dp)
                )

                Button(
                    onClick = onStartClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            MaterialTheme.colorScheme.primary,
                        contentColor =
                            MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "Empezar")
                }
            }

            Text(
                text = "Cristian Orellana - 25664",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharactersScreen(
    characters: List<Character>,
    onCharacterClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Characters")
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding =
                PaddingValues(vertical = 8.dp)
        ) {
            items(
                items = characters,
                key = { character ->
                    character.id
                }
            ) { character ->
                CharacterRow(
                    character = character,
                    onClick = {
                        onCharacterClick(character.id)
                    }
                )
            }
        }
    }
}

@Composable
private fun CharacterRow(
    character: Character,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = character.image,
                contentDescription =
                    "Imagen de ${character.name}",
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(start = 16.dp),
                verticalArrangement =
                    Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = character.name,
                    style =
                        MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text =
                        "${character.species} · ${character.status}",
                    style =
                        MaterialTheme.typography.bodyMedium,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = character.gender,
                    style =
                        MaterialTheme.typography.bodySmall,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        HorizontalDivider(
            color =
                MaterialTheme.colorScheme.outlineVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterDetailsScreen(
    character: Character,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Characters details")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(
                    horizontal = 28.dp,
                    vertical = 24.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = character.image,
                contentDescription =
                    "Imagen de ${character.name}",
                modifier = Modifier
                    .size(220.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = character.name,
                style =
                    MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            DetailRow(
                label = "Species",
                value = character.species
            )

            DetailRow(
                label = "Status",
                value = character.status
            )

            DetailRow(
                label = "Gender",
                value = character.gender
            )
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyLarge,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}