package uvg.moviles.rickymorty.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.userPreferencesDataStore by preferencesDataStore(
    name = "user_preferences"
)

class UserPreferencesRepository(context: Context) {
    private val dataStore = context.applicationContext.userPreferencesDataStore

    val userName: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(androidx.datastore.preferences.core.emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences -> preferences[NAME_KEY]?.takeIf { it.isNotBlank() } }

    suspend fun saveUserName(name: String) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = name.trim()
        }
    }

    suspend fun clearUserName() {
        dataStore.edit { preferences ->
            preferences.remove(NAME_KEY)
        }
    }

    private companion object {
        val NAME_KEY = stringPreferencesKey("name")
    }
}
