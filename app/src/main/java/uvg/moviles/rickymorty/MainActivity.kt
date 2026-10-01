package uvg.moviles.rickymorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import uvg.moviles.rickymorty.navigation.RickAndMortyApp
import uvg.moviles.rickymorty.ui.theme.RickYMortyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RickYMortyTheme {
                RickAndMortyApp()
            }
        }
    }
}
