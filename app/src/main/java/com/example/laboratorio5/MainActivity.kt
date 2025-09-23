package com.example.laboratorio5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.laboratorio5.ui.theme.Laboratorio5Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

// Modelos de datos
data class Pokemon(
    val id: Int,
    val name: String,
    val url: String,
    val imageUrl: String = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
)

data class PokemonDetail(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: PokemonSprites
)

data class PokemonSprites(
    val front_default: String?,
    val back_default: String?,
    val front_shiny: String?,
    val back_shiny: String?
)

data class PokemonListResponse(
    val results: List<PokemonBasic>
)

data class PokemonBasic(
    val name: String,
    val url: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Laboratorio5Theme {
                PokemonApp()
            }
        }
    }
}

@Composable
fun PokemonApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainFragment(navController)
        }
        composable("detail/{pokemonId}") { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getString("pokemonId")?.toIntOrNull() ?: 1
            DetailFragment(navController, pokemonId)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainFragment(navController: NavController) {
    var pokemonList by remember { mutableStateOf<List<Pokemon>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val response = withContext(Dispatchers.IO) {
                val url = URL("https://pokeapi.co/api/v2/pokemon?limit=100")
                val jsonString = url.readText()
                parseJsonResponse(jsonString)
            }
            pokemonList = response
            isLoading = false
        } catch (e: Exception) {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Pokedex",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EA)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    items(pokemonList) { pokemon ->
                        PokemonListItem(pokemon = pokemon) {
                            navController.navigate("detail/${pokemon.id}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PokemonListItem(pokemon: Pokemon, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = pokemon.name,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = pokemon.name.replaceFirstChar { it.titlecase() },
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
        }
    }

    Divider(
        color = Color(0xFFE0E0E0),
        thickness = 1.dp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailFragment(navController: NavController, pokemonId: Int) {
    var pokemonDetail by remember { mutableStateOf<PokemonDetail?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(pokemonId) {
        try {
            val detail = withContext(Dispatchers.IO) {
                val url = URL("https://pokeapi.co/api/v2/pokemon/$pokemonId")
                val jsonString = url.readText()
                parsePokemonDetail(jsonString)
            }
            pokemonDetail = detail
            isLoading = false
        } catch (e: Exception) {
            errorMessage = "Error: ${e.message}"
            isLoading = false

            // Crear un PokemonDetail básico con URLs generadas
            val basicDetail = PokemonDetail(
                id = pokemonId,
                name = "Pokemon #$pokemonId",
                height = 0,
                weight = 0,
                sprites = PokemonSprites(
                    front_default = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId.png",
                    back_default = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/$pokemonId.png",
                    front_shiny = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/$pokemonId.png",
                    back_shiny = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/shiny/$pokemonId.png"
                )
            )
            pokemonDetail = basicDetail
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detalle del Pokemon",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EA)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                pokemonDetail?.let { detail ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Título del Pokémon
                        Text(
                            text = detail.name.replaceFirstChar { it.titlecase() },
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )

                        // Grid de imágenes 2x2
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                // Front
                                PokemonImageCard(
                                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${detail.id}.png",
                                    label = "Front"
                                )
                                // Back
                                PokemonImageCard(
                                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/${detail.id}.png",
                                    label = "Back"
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                // Front Shiny
                                PokemonImageCard(
                                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/${detail.id}.png",
                                    label = "Front Shiny"
                                )
                                // Back Shiny
                                PokemonImageCard(
                                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/shiny/${detail.id}.png",
                                    label = "Back Shiny"
                                )
                            }
                        }

                        // Mostrar error si existe
                        errorMessage?.let { error ->
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Using fallback images",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PokemonImageCard(imageUrl: String?, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.size(120.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (imageUrl != null) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = label,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

// Funciones para parsear JSON manualmente (simplificadas)
fun parseJsonResponse(jsonString: String): List<Pokemon> {
    val pokemonList = mutableListOf<Pokemon>()

    try {
        // Extraer la parte de "results"
        val resultsStart = jsonString.indexOf("\"results\":[") + 11
        val resultsEnd = jsonString.indexOf("]", resultsStart)
        val resultsJson = jsonString.substring(resultsStart, resultsEnd)

        // Dividir por objetos
        val objects = resultsJson.split("},")

        objects.forEachIndexed { index, obj ->
            try {
                val cleanObj = obj.replace("{", "").replace("}", "")
                val nameStart = cleanObj.indexOf("\"name\":\"") + 8
                val nameEnd = cleanObj.indexOf("\"", nameStart)
                val name = cleanObj.substring(nameStart, nameEnd)

                val urlStart = cleanObj.indexOf("\"url\":\"") + 7
                val urlEnd = cleanObj.indexOf("\"", urlStart)
                val url = cleanObj.substring(urlStart, urlEnd)

                // Extraer ID de la URL
                val id = url.split("/").filter { it.isNotEmpty() }.last().toIntOrNull() ?: (index + 1)

                pokemonList.add(Pokemon(id, name, url))
            } catch (e: Exception) {
                // Ignorar errores de parseo individual
            }
        }
    } catch (e: Exception) {
        // Si falla el parseo, crear lista básica
        repeat(100) { i ->
            pokemonList.add(Pokemon(i + 1, "pokemon-${i + 1}", ""))
        }
    }

    return pokemonList
}

fun parsePokemonDetail(jsonString: String): PokemonDetail {
    try {
        // Extraer ID
        val idPattern = "\"id\":"
        val idStart = jsonString.indexOf(idPattern) + idPattern.length
        val idEnd = jsonString.indexOf(",", idStart)
        val id = jsonString.substring(idStart, idEnd).trim().toInt()

        // Extraer name
        val namePattern = "\"name\":\""
        val nameStart = jsonString.indexOf(namePattern) + namePattern.length
        val nameEnd = jsonString.indexOf("\"", nameStart)
        val name = jsonString.substring(nameStart, nameEnd)

        // Para sprites, usar URLs construidas directamente
        val sprites = PokemonSprites(
            front_default = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png",
            back_default = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/$id.png",
            front_shiny = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/$id.png",
            back_shiny = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/shiny/$id.png"
        )

        return PokemonDetail(id, name, 0, 0, sprites)
    } catch (e: Exception) {
        // Fallback con ID 1 si todo falla
        return PokemonDetail(
            1, "pokemon", 0, 0,
            PokemonSprites(
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/1.png",
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/1.png",
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/shiny/1.png"
            )
        )
    }
}