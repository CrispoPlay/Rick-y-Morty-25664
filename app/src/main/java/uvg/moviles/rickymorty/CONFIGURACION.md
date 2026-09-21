# Configuración del laboratorio 7

El archivo `MainActivity.kt` contiene las tres pantallas y toda la navegación solicitada. Copia también `Character.kt` y `CharacterDb.kt` al mismo paquete.

## 1. Ajustes obligatorios

- Cambia `package com.example.rickandmorty` por el package real de tu proyecto en los tres archivos Kotlin.
- En `LoginScreen`, reemplaza `TU NOMBRE - TU CARNÉ` con tus datos.
- Guarda un logo PNG como `app/src/main/res/drawable/rick_and_morty_logo.png`. El nombre debe estar en minúsculas y sin espacios.

## 2. Plugins y dependencias

En `app/build.gradle.kts`, agrega el plugin de serialización. Usa la misma versión de Kotlin que ya tiene tu proyecto:

```kotlin
plugins {
    id("org.jetbrains.kotlin.plugin.serialization") version "TU_VERSION_DE_KOTLIN"
}
```

Dentro de `dependencies` agrega:

```kotlin
implementation("androidx.navigation:navigation-compose:2.8.9")
implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
implementation("io.coil-kt:coil-compose:2.7.0")
implementation("androidx.compose.material:material-icons-extended")
```

Si el proyecto usa un catálogo `libs.versions.toml`, puedes declarar ahí las mismas librerías; no las dupliques.

## 3. Permiso de Internet

En `app/src/main/AndroidManifest.xml`, agrega esta línea directamente dentro de `<manifest>` y antes de `<application>`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

Coil necesita este permiso para descargar las imágenes.

## 4. Archivos esperados

```text
app/src/main/java/com/example/rickandmorty/
├── MainActivity.kt
├── Character.kt
└── CharacterDb.kt

app/src/main/res/drawable/
└── rick_and_morty_logo.png
```

## 5. Por qué cumple el enunciado

- Hay una sola Activity y las tres pantallas son composables.
- `Login`, `Characters` y `CharacterDetails` son destinos anotados con `@Serializable`.
- Al detalle solo se envía `characterId: Int`; el personaje se obtiene nuevamente con `CharacterDb.getCharacterById`.
- Al entrar al listado, `Login` se elimina mediante `popUpTo<Login> { inclusive = true }`.
- Atrás desde el listado llama a `finish()`; al abrir otra vez se inicia en Login.
- Atrás desde detalles vuelve al listado.
- Las imágenes remotas se cargan con `AsyncImage` de Coil.
- TopAppBar y botones usan colores de `MaterialTheme`.

## 6. Git y APK

Desde la raíz del proyecto, crea el repositorio y la rama requerida:

```bash
git init
git add .
git commit -m "Laboratorio 7 Rick and Morty"
git branch -M laboratorio7
```

Crea un repositorio nuevo y vacío en GitHub, conecta el remoto que GitHub te muestre y publica la rama:

```bash
git remote add origin URL_DE_TU_REPOSITORIO
git push -u origin laboratorio7
```

Genera el APK desde Android Studio con **Build > Build APK(s)**. Usualmente queda en:

```text
app/build/outputs/apk/debug/app-debug.apk
```

En la entrega, adjunta ese APK y coloca como comentario la URL del repositorio. Antes de entregar, confirma en GitHub que la rama visible sea `laboratorio7` y contenga el código.
