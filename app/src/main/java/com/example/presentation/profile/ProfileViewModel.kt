package com.example.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Achievement
import com.example.data.model.User
import com.example.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    val currentUser: StateFlow<User?> = userRepository.currentUserFlow

    private val _achievements = MutableStateFlow<List<Achievement>>(emptyList())
    val achievements: StateFlow<List<Achievement>> = _achievements.asStateFlow()

    init {
        observeUserAndAchievements()
    }

    private fun observeUserAndAchievements() {
        viewModelScope.launch {
            userRepository.currentUserFlow.collect {
                _achievements.value = userRepository.getAllBadges()
            }
        }
    }

    fun logout() {
        userRepository.logout()
    }
}
