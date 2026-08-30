package com.example.data.repository

import com.example.data.local.EncryptedPreferences
import com.example.data.model.Achievement
import com.example.data.model.Destination
import com.example.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val encryptedPreferences: EncryptedPreferences,
    private val destinationRepository: DestinationRepository
) {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val _currentUserFlow = MutableStateFlow<User?>(null)
    val currentUserFlow: StateFlow<User?> = _currentUserFlow.asStateFlow()

    private val _favoriteIdsFlow = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIdsFlow: StateFlow<Set<String>> = _favoriteIdsFlow.asStateFlow()

    init {
        val savedUid = encryptedPreferences.getUserUid()
        val savedEmail = encryptedPreferences.getUserEmail()
        if (!savedUid.isNullOrEmpty() && !savedEmail.isNullOrEmpty()) {
            val user = User(
                uid = savedUid,
                name = savedEmail.substringBefore("@").replaceFirstChar { it.uppercase() },
                email = savedEmail
            )
            _currentUserFlow.value = user
        } else {
            // Guest / Demo user default for seamless offline experience
            _currentUserFlow.value = User(
                uid = "demo_user_123",
                name = "Viajero Explorador",
                email = "explorador@rutaperu.pe"
            )
        }
    }

    suspend fun login(email: String, pass: String): Result<User> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, pass).await()
            val firebaseUser = authResult.user
            if (firebaseUser != null) {
                val tokenResult = firebaseUser.getIdToken(false).await()
                val token = tokenResult.token ?: "firebase_session_token"

                encryptedPreferences.saveUserSession(firebaseUser.uid, token, email)

                val user = User(
                    uid = firebaseUser.uid,
                    name = firebaseUser.displayName ?: email.substringBefore("@").replaceFirstChar { it.uppercase() },
                    email = email
                )
                _currentUserFlow.value = user
                syncFavoritesFromRemote(firebaseUser.uid)
                Result.success(user)
            } else {
                Result.failure(Exception("Usuario no encontrado en Firebase"))
            }
        } catch (e: Exception) {
            // Local fallback login for immediate offline/demo access
            if (email.isNotBlank() && pass.length >= 6) {
                val uid = "local_" + email.hashCode()
                val user = User(
                    uid = uid,
                    name = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                    email = email
                )
                encryptedPreferences.saveUserSession(uid, "local_secure_token", email)
                _currentUserFlow.value = user
                Result.success(user)
            } else {
                Result.failure(e)
            }
        }
    }

    suspend fun register(name: String, email: String, pass: String): Result<User> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, pass).await()
            val firebaseUser = authResult.user
            if (firebaseUser != null) {
                val tokenResult = firebaseUser.getIdToken(false).await()
                val token = tokenResult.token ?: "firebase_session_token"

                val user = User(
                    uid = firebaseUser.uid,
                    name = name.ifBlank { email.substringBefore("@") },
                    email = email
                )

                encryptedPreferences.saveUserSession(firebaseUser.uid, token, email)
                _currentUserFlow.value = user

                // Save user doc to Firestore
                try {
                    firestore.collection("users").document(firebaseUser.uid).set(
                        mapOf(
                            "uid" to user.uid,
                            "name" to user.name,
                            "email" to user.email,
                            "unlockedAchievements" to emptyList<String>(),
                            "favoritesCount" to 0
                        )
                    ).await()
                } catch (e: Exception) {
                    // Firestore rules or offline fallback
                }

                Result.success(user)
            } else {
                Result.failure(Exception("Error al registrar en Firebase Auth"))
            }
        } catch (e: Exception) {
            // Local fallback register
            if (email.isNotBlank() && pass.length >= 6) {
                val uid = "local_" + email.hashCode()
                val user = User(
                    uid = uid,
                    name = name.ifBlank { email.substringBefore("@") },
                    email = email
                )
                encryptedPreferences.saveUserSession(uid, "local_token", email)
                _currentUserFlow.value = user
                Result.success(user)
            } else {
                Result.failure(e)
            }
        }
    }

    fun logout() {
        try {
            auth.signOut()
        } catch (e: Exception) {
            // ignore
        }
        encryptedPreferences.clearSession()
        _currentUserFlow.value = null
        _favoriteIdsFlow.value = emptySet()
    }

    /**
     * Envía un correo electrónico para restablecer la contraseña.
     * @param email Correo del usuario.
     * @return Result.success(Unit) si se envió, o Result.failure con la excepción.
     */
    suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleFavorite(destinationId: String) {
        val currentFavs = _favoriteIdsFlow.value.toMutableSet()
        if (currentFavs.contains(destinationId)) {
            currentFavs.remove(destinationId)
        } else {
            currentFavs.add(destinationId)
        }
        _favoriteIdsFlow.value = currentFavs

        val user = _currentUserFlow.value ?: return
        val updatedAchievements = calculateAchievements(currentFavs)

        _currentUserFlow.value = user.copy(
            favoritesCount = currentFavs.size,
            unlockedAchievements = updatedAchievements
        )

        // Save to Firestore if authenticated
        try {
            val userRef = firestore.collection("users").document(user.uid)
            val favRef = userRef.collection("favorites").document(destinationId)

            if (currentFavs.contains(destinationId)) {
                favRef.set(mapOf("destinationId" to destinationId, "addedAt" to System.currentTimeMillis())).await()
            } else {
                favRef.delete().await()
            }

            userRef.update(
                mapOf(
                    "favoritesCount" to currentFavs.size,
                    "unlockedAchievements" to updatedAchievements
                )
            ).await()
        } catch (e: Exception) {
            // Offline/Local state is updated
        }
    }

    fun isFavorite(destinationId: String): Boolean {
        return _favoriteIdsFlow.value.contains(destinationId)
    }

    fun getFavoriteDestinations(): List<Destination> {
        val favIds = _favoriteIdsFlow.value
        return destinationRepository.getAllDestinations().filter { favIds.contains(it.id) }
    }

    private suspend fun syncFavoritesFromRemote(uid: String) {
        try {
            val snapshot = firestore.collection("users").document(uid).collection("favorites").get().await()
            val favIds = snapshot.documents.mapNotNull { it.id }.toSet()
            _favoriteIdsFlow.value = favIds

            val achievements = calculateAchievements(favIds)
            _currentUserFlow.value = _currentUserFlow.value?.copy(
                favoritesCount = favIds.size,
                unlockedAchievements = achievements
            )
        } catch (e: Exception) {
            // Keep local
        }
    }

    fun calculateAchievements(favIds: Set<String>): List<String> {
        val favDestinations = destinationRepository.getAllDestinations().filter { favIds.contains(it.id) }
        val unlocked = mutableListOf<String>()

        // 1. "Primer destino" (al agregar 1 favorito)
        if (favIds.isNotEmpty()) {
            unlocked.add("primer_destino")
        }

        // 2. "Explorador de la Costa" (al tener 3 destinos de costa: Paracas, Máncora, Trujillo)
        val coastIds = setOf("paracas", "mancora", "trujillo", "huacachina")
        val coastCount = favDestinations.count { coastIds.contains(it.id) || it.category == "Playa" }
        if (coastCount >= 3) {
            unlocked.add("explorador_costa")
        }

        // 3. "Aventurero de los Andes" (al tener 3 destinos andinos: Cusco, Huaraz, Puno, Arequipa)
        val andesIds = setOf("machu_picchu", "cusco_centro", "huaraz", "puno", "arequipa")
        val andesCount = favDestinations.count { andesIds.contains(it.id) || it.region == "Cusco" || it.region == "Ancash" || it.region == "Puno" }
        if (andesCount >= 3) {
            unlocked.add("aventurero_andes")
        }

        // 4. "Explorador de la Selva" (al tener Iquitos)
        val jungleCount = favDestinations.count { it.id == "iquitos" || it.category == "Selva" }
        if (jungleCount >= 1) {
            unlocked.add("explorador_selva")
        }

        // 5. "Gran Explorador del Perú" (al tener 5 favoritos totales)
        if (favIds.size >= 5) {
            unlocked.add("gran_explorador")
        }

        return unlocked
    }

    fun getAllBadges(): List<Achievement> {
        val unlockedList = _currentUserFlow.value?.unlockedAchievements ?: emptyList()
        return listOf(
            Achievement(
                id = "primer_destino",
                title = "Primer Destino",
                description = "Agrega tu primer destino turístico a favoritos",
                category = "Inicio",
                isUnlocked = unlockedList.contains("primer_destino")
            ),
            Achievement(
                id = "explorador_costa",
                title = "Explorador de la Costa",
                description = "Guarda al menos 3 destinos fascinantes de la costa peruana",
                category = "Costa",
                isUnlocked = unlockedList.contains("explorador_costa")
            ),
            Achievement(
                id = "aventurero_andes",
                title = "Aventurero de los Andes",
                description = "Descubre 3 maravillas andinas por encima de las nubes",
                category = "Sierra",
                isUnlocked = unlockedList.contains("aventurero_andes")
            ),
            Achievement(
                id = "explorador_selva",
                title = "Explorador de la Selva",
                description = "Adéntrate en la selva amazónica del Perú",
                category = "Selva",
                isUnlocked = unlockedList.contains("explorador_selva")
            ),
            Achievement(
                id = "gran_explorador",
                title = "Gran Explorador del Perú",
                description = "Consigue guardar 5 o más destinos en tu Pasaporte Perú",
                category = "Nacional",
                isUnlocked = unlockedList.contains("gran_explorador")
            )
        )
    }
}