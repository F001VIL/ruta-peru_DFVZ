package com.example.data.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val unlockedAchievements: List<String> = emptyList(),
    val favoritesCount: Int = 0
)
