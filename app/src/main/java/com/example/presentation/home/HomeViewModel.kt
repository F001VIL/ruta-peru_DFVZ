package com.example.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Destination
import com.example.data.repository.DestinationRepository
import com.example.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val destinationRepository: DestinationRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _featuredDestinations = MutableStateFlow<List<Destination>>(emptyList())
    val featuredDestinations: StateFlow<List<Destination>> = _featuredDestinations.asStateFlow()

    private val _popularDestinations = MutableStateFlow<List<Destination>>(emptyList())
    val popularDestinations: StateFlow<List<Destination>> = _popularDestinations.asStateFlow()

    val favoriteIds = userRepository.favoriteIdsFlow

    init {
        loadDestinations()
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        if (newQuery.isBlank()) {
            _popularDestinations.value = destinationRepository.getPopularDestinations()
        } else {
            _popularDestinations.value = destinationRepository.searchDestinations(newQuery)
        }
    }

    fun toggleFavorite(destinationId: String) {
        viewModelScope.launch {
            userRepository.toggleFavorite(destinationId)
        }
    }

    private fun loadDestinations() {
        _featuredDestinations.value = destinationRepository.getFeaturedDestinations()
        _popularDestinations.value = destinationRepository.getPopularDestinations()
    }
}
