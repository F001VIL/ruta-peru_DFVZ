package com.example.data.model

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val isUnlocked: Boolean = false
)
