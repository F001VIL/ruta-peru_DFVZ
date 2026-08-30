package com.example.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 1. ESTADOS ACTUALIZADOS (agregamos ResetPasswordSent)
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val email: String) : AuthState()  // Para login y registro
    object ResetPasswordSent : AuthState()               // NUEVO: Para cuando se envía el correo
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // FUNCIÓN LOGIN (ya la tienes)
    fun login(email: String, pass: String) {
        if (!isValidEmail(email)) {
            _authState.value = AuthState.Error("Por favor, ingrese un correo electrónico válido")
            return
        }
        if (pass.length < 6) {
            _authState.value = AuthState.Error("La contraseña debe tener al menos 6 caracteres")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = userRepository.login(email, pass)
            result.fold(
                onSuccess = { user ->
                    _authState.value = AuthState.Success(user.email)
                },
                onFailure = { error ->
                    _authState.value = AuthState.Error(error.localizedMessage ?: "Error al iniciar sesión")
                }
            )
        }
    }

    // FUNCIÓN REGISTRO (ya la tienes)
    fun register(name: String, email: String, pass: String, confirmPass: String) {
        if (name.isBlank()) {
            _authState.value = AuthState.Error("Por favor, ingrese su nombre completo")
            return
        }
        if (!isValidEmail(email)) {
            _authState.value = AuthState.Error("Por favor, ingrese un correo válido")
            return
        }
        if (pass.length < 6) {
            _authState.value = AuthState.Error("La contraseña debe tener al menos 6 caracteres")
            return
        }
        if (pass != confirmPass) {
            _authState.value = AuthState.Error("Las contraseñas no coinciden")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = userRepository.register(name, email, pass)
            result.fold(
                onSuccess = { user ->
                    _authState.value = AuthState.Success(user.email)
                },
                onFailure = { error ->
                    _authState.value = AuthState.Error(error.localizedMessage ?: "Error al registrarse")
                }
            )
        }
    }

    // 🆕 NUEVA FUNCIÓN: RECUPERAR CONTRASEÑA
    fun resetPassword(email: String) {
        if (!isValidEmail(email)) {
            _authState.value = AuthState.Error("Por favor, ingrese un correo electrónico válido")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = userRepository.resetPassword(email) // Llama al repositorio
            result.fold(
                onSuccess = {
                    _authState.value = AuthState.ResetPasswordSent // Estado de éxito
                },
                onFailure = { error ->
                    _authState.value = AuthState.Error(error.localizedMessage ?: "Error al enviar correo de recuperación")
                }
            )
        }
    }

    // FUNCIÓN INVITADO (ya la tienes)
    fun loginAsGuest() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            userRepository.login("invitado@rutaperu.pe", "123456")
            _authState.value = AuthState.Success("invitado@rutaperu.pe")
        }
    }

    // FUNCIÓN PARA LIMPIAR EL ESTADO
    fun resetState() {
        _authState.value = AuthState.Idle
    }

    // VALIDACIÓN DE EMAIL
    private fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}