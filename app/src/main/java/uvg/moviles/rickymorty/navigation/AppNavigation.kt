package uvg.moviles.rickymorty.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uvg.moviles.rickymorty.ui.screen.login.LoginScreen
import uvg.moviles.rickymorty.ui.screen.main.MainScreen

@Composable
fun RickAndMortyApp(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = LoginDestination) {
        composable<LoginDestination> {
            LoginScreen(
                onStartClick = {
                    navController.navigate(MainDestination) {
                        popUpTo<LoginDestination> { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<MainDestination> {
            MainScreen(
                onLogout = {
                    navController.navigate(LoginDestination) {
                        popUpTo<MainDestination> { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
