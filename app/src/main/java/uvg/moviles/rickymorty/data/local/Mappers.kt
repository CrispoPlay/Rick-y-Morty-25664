package uvg.moviles.rickymorty.data.local

import uvg.moviles.rickymorty.data.model.Character
import uvg.moviles.rickymorty.data.model.Location

fun CharacterEntity.toModel() = Character(id, name, status, species, gender, image, isFavorite)

fun Character.toEntity() = CharacterEntity(id, name, status, species, gender, image, isFavorite)

fun LocationEntity.toModel() = Location(id, name, type, dimension)

fun Location.toEntity() = LocationEntity(id, name, type, dimension)
