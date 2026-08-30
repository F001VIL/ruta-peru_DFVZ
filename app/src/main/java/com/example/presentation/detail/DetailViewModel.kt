package com.example.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Destination
import com.example.data.model.WeatherResponse
import com.example.data.repository.DestinationRepository
import com.example.data.repository.UserRepository
import com.example.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val destinationRepository: DestinationRepository,
    private val userRepository: UserRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _destination = MutableStateFlow<Destination?>(null)
    val destination: StateFlow<Destination?> = _destination.asStateFlow()

    private val _weatherState = MutableStateFlow<WeatherResponse?>(null)
    val weatherState: StateFlow<WeatherResponse?> = _weatherState.asStateFlow()

    private val _isLoadingWeather = MutableStateFlow(false)
    val isLoadingWeather: StateFlow<Boolean> = _isLoadingWeather.asStateFlow()

    val favoriteIds = userRepository.favoriteIdsFlow

    fun loadDestination(destinationId: String) {
        val dest = destinationRepository.getDestinationById(destinationId)
        _destination.value = dest

        if (dest != null) {
            loadWeather(dest.latitude, dest.longitude, dest.name)
        }
    }

    fun toggleFavorite() {
        val dest = _destination.value ?: return
        viewModelScope.launch {
            userRepository.toggleFavorite(dest.id)
        }
    }

    private fun loadWeather(lat: Double, lon: Double, cityName: String) {
        viewModelScope.launch {
            _isLoadingWeather.value = true
            val weather = weatherRepository.getWeatherForLocation(lat, lon, cityName)
            _weatherState.value = weather
            _isLoadingWeather.value = false
        }
    }
}
