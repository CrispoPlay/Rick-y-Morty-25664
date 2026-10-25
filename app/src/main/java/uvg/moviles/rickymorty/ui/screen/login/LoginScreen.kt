package uvg.moviles.rickymorty.ui.screen.login

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import uvg.moviles.rickymorty.R

@Composable
fun LoginScreen(
    onBack: () -> Unit,
    onLogin: (String) -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    BackHandler(enabled = !isLoading, onBack = onBack)

    var name by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val normalizedName = name.trim()
    val submit = {
        if (normalizedName.isNotEmpty() && !isLoading) {
            focusManager.clearFocus()
            onLogin(normalizedName)
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp, vertical = 24.dp)) {
            Column(
                modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.rick_and_morty_logo),
                    contentDescription = "Logo de Rick and Morty",
                    modifier = Modifier.fillMaxWidth(0.85f).height(150.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(Modifier.height(20.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nombre") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { submit() })
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = submit,
                    enabled = normalizedName.isNotEmpty() && !isLoading,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Iniciar sesión")
                    }
                }
                if (isLoading) {
                    Spacer(Modifier.height(12.dp))
                    Text("Sincronizando datos locales…")
                }
                errorMessage?.let {
                    Spacer(Modifier.height(12.dp))
                    Text(it, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
                }
            }
            Text(
                text = "Cristian Estuardo Orellana Dieguez - 25664",
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
