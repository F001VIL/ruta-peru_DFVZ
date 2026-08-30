package com.example.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Destination
import com.example.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _favoriteDestinations = MutableStateFlow<List<Destination>>(emptyList())
    val favoriteDestinations: StateFlow<List<Destination>> = _favoriteDestinations.asStateFlow()

    init {
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            userRepository.favoriteIdsFlow.collect {
                _favoriteDestinations.value = userRepository.getFavoriteDestinations()
            }
        }
    }

    fun removeFavorite(destinationId: String) {
        viewModelScope.launch {
            userRepository.toggleFavorite(destinationId)
        }
    }
}
