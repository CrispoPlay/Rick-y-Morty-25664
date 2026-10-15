package uvg.moviles.rickymorty.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import uvg.moviles.rickymorty.data.UserPreferencesRepository
import uvg.moviles.rickymorty.ui.screen.login.LoginScreen
import uvg.moviles.rickymorty.ui.screen.main.MainScreen

@Composable
fun RickAndMortyApp(
    onExitApp: () -> Unit = {},
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val preferencesRepository = remember(context) { UserPreferencesRepository(context) }
    val coroutineScope = rememberCoroutineScope()
    var userName by remember { mutableStateOf<String?>(null) }
    var sessionChecked by remember { mutableStateOf(false) }

    LaunchedEffect(preferencesRepository) {
        userName = preferencesRepository.userName.first()
        sessionChecked = true
    }

    NavHost(navController = navController, startDestination = SessionDestination) {
        composable<SessionDestination> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

            LaunchedEffect(sessionChecked, userName) {
                if (!sessionChecked) return@LaunchedEffect

                val destination = if (userName == null) LoginDestination else MainDestination
                navController.navigate(destination) {
                    popUpTo<SessionDestination> { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
        composable<LoginDestination> {
            LoginScreen(
                onBack = onExitApp,
                onLogin = { name ->
                    coroutineScope.launch {
                        preferencesRepository.saveUserName(name)
                        userName = name
                        navController.navigate(MainDestination) {
                            popUpTo<LoginDestination> { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
        composable<MainDestination> {
            MainScreen(
                userName = userName.orEmpty(),
                onExit = onExitApp,
                onLogout = {
                    coroutineScope.launch {
                        preferencesRepository.clearUserName()
                        userName = null
                        navController.navigate(LoginDestination) {
                            popUpTo<MainDestination> { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}
