package com.example.data.repository

import com.example.data.model.Itinerary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ItineraryRepository {

    private val _savedItineraries = MutableStateFlow<List<Itinerary>>(emptyList())
    val savedItineraries: StateFlow<List<Itinerary>> = _savedItineraries.asStateFlow()

    fun saveItinerary(itinerary: Itinerary) {
        val current = _savedItineraries.value.toMutableList()
        current.add(0, itinerary)
        _savedItineraries.value = current
    }

    fun deleteItinerary(id: String) {
        _savedItineraries.value = _savedItineraries.value.filter { it.id != id }
    }
}
