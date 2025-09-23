## Laboratorio 5 - Programación de Plataformas Móviles
Una aplicación Android moderna desarrollada con Jetpack Compose que permite explorar los primeros 100 Pokémon utilizando la PokéAPI.

### 📱 Características
- Visualiza los primeros 100 Pokémon con imagen, nombre y número de Pokédex
- Navega entre la lista principal y los detalles de cada Pokémon
  Detalles del Pokémon: Vista detallada con múltiples sprites (normal, shiny, frontal, trasero)
- Interfaz diseñada con Material Design 3 y Jetpack Compose
- Carga asíncrona con imágenes cargadas de forma eficiente con Coil

### 🛠️ Tecnologías Utilizadas
- ***Kotlin:*** Lenguaje de programación principal
- ***Jetpack Compose:*** Framework de UI moderno para Android
- ***Material Design 3:*** Sistema de diseño de Google
- ***Coil:*** Librería para carga de imágenes
- ***Navigation Compose:*** Navegación entre pantallas
- ***Coroutines:*** Programación asíncrona
- ***PokéAPI:*** API REST para datos de Pokémon

### 📋 Requisitos
- Android Studio Arctic Fox o superior
- SDK de Android 21 o superior
- Kotlin 1.8.0 o superior
- Conexión a internet para cargar datos y sprites

### 🚀 Instalación
**1. Clonar el repositorio**
```bash
https://github.com/MarceloDetlefsen/Laboratorio_5_PM.git
cd Laboratorio5_PM
```
**2. Abrir en Android Studio**

Abrir Android Studio
Seleccionar "Open an existing project"
Navegar hasta la carpeta del proyecto

**3. Sincronizar dependencias**

Android Studio automáticamente sincronizará las dependencias de Gradle

### 🎨 Diseño

**Colores**

- Color primario: Rojo (#DC143C) - Inspirado en las Pokéballs
- Fondo: Blanco para una experiencia limpia
- Texto: Negro para máxima legibilidad

**Componentes UI**

- TopAppBar: Barra superior con título y navegación
- LazyColumn: Lista optimizada para scroll fluido
- Cards: Contenedores para elementos de la lista
- AsyncImage: Carga eficiente de imágenes de sprites

### 🌐 API Utilizada
PokéAPI: https://pokeapi.co/api/v2/pokemon

- Endpoint principal: /pokemon?limit=100
- Sprites: https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/

**Tipos de Sprites**

- Normal frontal: pokemon/{id}.png
- Normal trasero: pokemon/back/{id}.png
- Shiny frontal: pokemon/shiny/{id}.png
- Shiny trasero: pokemon/back/shiny/{id}.png

### 📷 Capturas del Programa

**Lista de Pokemones**

![Menu.png](Menu.png)

**Vista Pokemon Específico**

![Especifico.png](Especifico.png)