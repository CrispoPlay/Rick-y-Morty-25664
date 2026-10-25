package uvg.moviles.rickymorty.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CharacterEntity::class, LocationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RickAndMortyDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun locationDao(): LocationDao

    companion object {
        @Volatile private var instance: RickAndMortyDatabase? = null

        fun getInstance(context: Context): RickAndMortyDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    RickAndMortyDatabase::class.java,
                    "rick_and_morty.db"
                ).build().also { instance = it }
            }
    }
}
