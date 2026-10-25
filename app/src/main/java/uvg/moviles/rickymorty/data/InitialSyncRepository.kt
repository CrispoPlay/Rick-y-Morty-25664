package uvg.moviles.rickymorty.data

import androidx.room.withTransaction
import kotlinx.coroutines.delay
import uvg.moviles.rickymorty.data.local.RickAndMortyDatabase
import uvg.moviles.rickymorty.data.local.toEntity

class InitialSyncRepository(private val database: RickAndMortyDatabase) {
    suspend fun syncIfNeeded(simulateLoading: Boolean = true) {
        val alreadySynced = database.withTransaction {
            database.characterDao().count() > 0 && database.locationDao().count() > 0
        }
        if (alreadySynced) return

        if (simulateLoading) delay(4_000)
        database.withTransaction {
            database.characterDao().insertAll(CharacterDb().getAllCharacters().map { it.toEntity() })
            database.locationDao().insertAll(LocationDb().getAllLocations().map { it.toEntity() })
        }
    }

    suspend fun clearAll() {
        database.withTransaction {
            database.characterDao().deleteAll()
            database.locationDao().deleteAll()
        }
    }
}
