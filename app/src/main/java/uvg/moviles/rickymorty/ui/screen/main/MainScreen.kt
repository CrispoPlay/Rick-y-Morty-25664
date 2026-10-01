package uvg.moviles.rickymorty.ui.screen.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import uvg.moviles.rickymorty.data.CharacterDb
import uvg.moviles.rickymorty.data.LocationDb
import uvg.moviles.rickymorty.navigation.CharacterDetailsDestination
import uvg.moviles.rickymorty.navigation.CharactersDestination
import uvg.moviles.rickymorty.navigation.CharactersGraph
import uvg.moviles.rickymorty.navigation.LocationDetailsDestination
import uvg.moviles.rickymorty.navigation.LocationsDestination
import uvg.moviles.rickymorty.navigation.LocationsGraph
import uvg.moviles.rickymorty.navigation.ProfileDestination
import uvg.moviles.rickymorty.ui.screen.characters.CharacterDetailsScreen
import uvg.moviles.rickymorty.ui.screen.characters.CharactersScreen
import uvg.moviles.rickymorty.ui.screen.locations.LocationDetailsScreen
import uvg.moviles.rickymorty.ui.screen.locations.LocationsScreen
import uvg.moviles.rickymorty.ui.screen.profile.ProfileScreen

private enum class MainTab(val label: String, val icon: ImageVector) {
    Characters("Characters", Icons.Filled.Groups),
    Locations("Locations", Icons.Filled.Public),
    Profile("Profile", Icons.Filled.Person)
}

@Composable
fun MainScreen(onLogout: () -> Unit) {
    val navController = rememberNavController()
    val characterDb = remember { CharacterDb() }
    val locationDb = remember { LocationDb() }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                MainTab.entries.forEach { tab ->
                    val selected = when (tab) {
                        MainTab.Characters -> currentDestination?.hierarchy?.any { it.hasRoute<CharactersGraph>() } == true
                        MainTab.Locations -> currentDestination?.hierarchy?.any { it.hasRoute<LocationsGraph>() } == true
                        MainTab.Profile -> currentDestination?.hasRoute<ProfileDestination>() == true
                    }
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            val navigateToTab = {
                                navController.graph.findStartDestination().id
                            }
                            when (tab) {
                                MainTab.Characters -> navController.navigate(CharactersGraph) {
                                    popUpTo(navigateToTab()) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                                MainTab.Locations -> navController.navigate(LocationsGraph) {
                                    popUpTo(navigateToTab()) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                                MainTab.Profile -> navController.navigate(ProfileDestination) {
                                    popUpTo(navigateToTab()) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = CharactersGraph,
            modifier = Modifier.padding(innerPadding)
        ) {
            navigation<CharactersGraph>(startDestination = CharactersDestination) {
                composable<CharactersDestination> {
                    CharactersScreen(
                        characters = characterDb.getAllCharacters(),
                        onCharacterClick = { navController.navigate(CharacterDetailsDestination(it)) }
                    )
                }
                composable<CharacterDetailsDestination> { entry ->
                    val destination = entry.toRoute<CharacterDetailsDestination>()
                    CharacterDetailsScreen(
                        character = characterDb.getCharacterById(destination.characterId),
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
            navigation<LocationsGraph>(startDestination = LocationsDestination) {
                composable<LocationsDestination> {
                    LocationsScreen(
                        locations = locationDb.getAllLocations(),
                        onLocationClick = { navController.navigate(LocationDetailsDestination(it)) }
                    )
                }
                composable<LocationDetailsDestination> { entry ->
                    val destination = entry.toRoute<LocationDetailsDestination>()
                    LocationDetailsScreen(
                        location = locationDb.getLocationById(destination.locationId),
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
            composable<ProfileDestination> {
                ProfileScreen(onLogout = onLogout)
            }
        }
    }
}
