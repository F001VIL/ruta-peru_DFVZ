package com.example.data.model

data class Destination(
    val id: String = "",
    val name: String = "",
    val region: String = "",
    val category: String = "", // Naturaleza, Cultura, Aventura, Playa, Selva
    val rating: Double = 4.8,
    val description: String = "",
    val history: String = "",
    val activities: List<String> = emptyList(),
    val imageUrl: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isFeatured: Boolean = false,
    val isPopular: Boolean = false
)
