# Laboratorio 8: navegación avanzada

El proyecto conserva una sola `MainActivity` y organiza cada pantalla en su propio archivo dentro de `ui/screen`.

## Navegación

- Todos los destinos están declarados con `@Serializable` en `navigation/Destinations.kt`.
- El grafo raíz contiene Login y la shell principal.
- Characters y Locations poseen grafos anidados independientes.
- Profile es un destino directo, sin grafo propio.
- Los detalles reciben únicamente el ID y consultan el objeto en su base de datos local.
- Cerrar sesión reemplaza la shell principal por Login y elimina el back stack autenticado.

## Estructura principal

```text
data/
├── CharacterDb.kt
├── LocationDb.kt
└── model/
navigation/
├── AppNavigation.kt
└── Destinations.kt
ui/
├── components/
└── screen/
    ├── characters/
    ├── locations/
    ├── login/
    ├── main/
    └── profile/
```

## Compilación y entrega

La branch del laboratorio es `codex/laboratorio8`. Para generar el APK:

```powershell
.\gradlew.bat assembleDebug
```

El APK queda en `app/build/outputs/apk/debug/app-debug.apk`. En Canvas se debe adjuntar ese archivo y colocar como comentario la URL del repositorio apuntando a la branch del Laboratorio 8.
