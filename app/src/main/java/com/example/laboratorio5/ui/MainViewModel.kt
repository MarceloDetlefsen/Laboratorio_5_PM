package com.example.laboratorio5.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laboratorio5.data.repository.MainRepository
import com.example.laboratorio5.data.model.Pokemon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MainUiState(
    val pokemonList: List<Pokemon> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class MainViewModel(
    private val repository: MainRepository = MainRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        loadPokemonList()
    }

    private fun loadPokemonList() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val pokemonList = repository.getPokemonList()
                _uiState.value = _uiState.value.copy(
                    pokemonList = pokemonList,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al cargar la lista de Pokémon: ${e.message}"
                )
            }
        }
    }

    fun retryLoadPokemonList() {
        loadPokemonList()
    }
}