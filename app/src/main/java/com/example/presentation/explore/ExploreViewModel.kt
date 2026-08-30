package com.example.presentation.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Destination
import com.example.data.repository.DestinationRepository
import com.example.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExploreViewModel(
    private val destinationRepository: DestinationRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow("Todos")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _filteredDestinations = MutableStateFlow<List<Destination>>(emptyList())
    val filteredDestinations: StateFlow<List<Destination>> = _filteredDestinations.asStateFlow()

    val favoriteIds = userRepository.favoriteIdsFlow

    val categories = listOf("Todos", "Naturaleza", "Cultura", "Aventura", "Playa", "Selva")

    init {
        filterByCategory("Todos")
    }

    fun filterByCategory(category: String) {
        _selectedCategory.value = category
        _filteredDestinations.value = destinationRepository.searchDestinations("", category)
    }

    fun toggleFavorite(destinationId: String) {
        viewModelScope.launch {
            userRepository.toggleFavorite(destinationId)
        }
    }
}
