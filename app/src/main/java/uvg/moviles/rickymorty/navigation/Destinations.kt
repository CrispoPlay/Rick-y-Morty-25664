package uvg.moviles.rickymorty.navigation

import kotlinx.serialization.Serializable

@Serializable data object LoginDestination
@Serializable data object MainDestination
@Serializable data object CharactersGraph
@Serializable data object CharactersDestination
@Serializable data class CharacterDetailsDestination(val characterId: Int)
@Serializable data object LocationsGraph
@Serializable data object LocationsDestination
@Serializable data class LocationDetailsDestination(val locationId: Int)
@Serializable data object ProfileDestination
