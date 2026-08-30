package com.example.presentation.myroute

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Destination
import com.example.data.model.Itinerary
import com.example.data.repository.DestinationRepository
import com.example.data.repository.ItineraryRepository
import com.example.domain.usecase.GenerateItineraryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyRouteViewModel(
    private val destinationRepository: DestinationRepository,
    private val itineraryRepository: ItineraryRepository,
    private val generateItineraryUseCase: GenerateItineraryUseCase
) : ViewModel() {

    private val _currentStep = MutableStateFlow(1)
    val currentStep: StateFlow<Int> = _currentStep.asStateFlow()

    private val _selectedDestinationName = MutableStateFlow("Machu Picchu")
    val selectedDestinationName: StateFlow<String> = _selectedDestinationName.asStateFlow()

    private val _daysCount = MutableStateFlow(3)
    val daysCount: StateFlow<Int> = _daysCount.asStateFlow()

    private val _selectedBudget = MutableStateFlow("Medio")
    val selectedBudget: StateFlow<String> = _selectedBudget.asStateFlow()

    private val _selectedInterests = MutableStateFlow<Set<String>>(setOf("Cultura", "Naturaleza"))
    val selectedInterests: StateFlow<Set<String>> = _selectedInterests.asStateFlow()

    private val _generatedItinerary = MutableStateFlow<Itinerary?>(null)
    val generatedItinerary: StateFlow<Itinerary?> = _generatedItinerary.asStateFlow()

    val savedItineraries: StateFlow<List<Itinerary>> = itineraryRepository.savedItineraries

    val allDestinations: List<Destination> = destinationRepository.getAllDestinations()

    val availableInterests = listOf("Naturaleza", "Cultura", "Aventura", "Gastronomía", "Playa", "Fotografía")
    val budgetOptions = listOf("Bajo", "Medio", "Alto")

    fun setSelectedDestination(name: String) {
        _selectedDestinationName.value = name
    }

    fun setDaysCount(days: Int) {
        _daysCount.value = days
    }

    fun setBudget(budget: String) {
        _selectedBudget.value = budget
    }

    fun toggleInterest(interest: String) {
        val current = _selectedInterests.value.toMutableSet()
        if (current.contains(interest)) {
            if (current.size > 1) current.remove(interest)
        } else {
            current.add(interest)
        }
        _selectedInterests.value = current
    }

    fun nextStep() {
        if (_currentStep.value < 3) {
            _currentStep.value += 1
        }
    }

    fun previousStep() {
        if (_currentStep.value > 1) {
            _currentStep.value -= 1
        }
    }

    fun generateItinerary() {
        val itinerary = generateItineraryUseCase.execute(
            destinationName = _selectedDestinationName.value,
            daysCount = _daysCount.value,
            budget = _selectedBudget.value,
            interests = _selectedInterests.value.toList()
        )
        _generatedItinerary.value = itinerary
        itineraryRepository.saveItinerary(itinerary)
    }

    fun resetPlanner() {
        _currentStep.value = 1
        _generatedItinerary.value = null
    }

    fun preselectDestination(destinationId: String) {
        val dest = destinationRepository.getDestinationById(destinationId)
        if (dest != null) {
            _selectedDestinationName.value = dest.name
        }
    }
}
