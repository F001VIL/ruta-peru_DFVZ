package com.example.di

import android.content.Context
import com.example.data.local.EncryptedPreferences
import com.example.data.repository.DestinationRepository
import com.example.data.repository.ItineraryRepository
import com.example.data.repository.UserRepository
import com.example.data.repository.WeatherRepository
import com.example.domain.usecase.GenerateItineraryUseCase

class AppContainer(context: Context) {

    val encryptedPreferences: EncryptedPreferences by lazy {
        EncryptedPreferences(context)
    }

    val destinationRepository: DestinationRepository by lazy {
        DestinationRepository()
    }

    val userRepository: UserRepository by lazy {
        UserRepository(encryptedPreferences, destinationRepository)
    }

    val weatherRepository: WeatherRepository by lazy {
        WeatherRepository()
    }

    val itineraryRepository: ItineraryRepository by lazy {
        ItineraryRepository()
    }

    val generateItineraryUseCase: GenerateItineraryUseCase by lazy {
        GenerateItineraryUseCase()
    }

    companion object {
        @Volatile
        private var instance: AppContainer? = null

        fun getInstance(context: Context): AppContainer {
            return instance ?: synchronized(this) {
                instance ?: AppContainer(context.applicationContext).also { instance = it }
            }
        }
    }
}
