package com.example.data.model

data class ItineraryDay(
    val dayNumber: Int,
    val title: String,
    val activities: List<String>
)

data class Itinerary(
    val id: String = "",
    val destinationName: String,
    val daysCount: Int,
    val budget: String,
    val interests: List<String>,
    val days: List<ItineraryDay>
)
