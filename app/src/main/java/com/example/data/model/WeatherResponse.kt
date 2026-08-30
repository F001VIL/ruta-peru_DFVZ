package com.example.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    @Json(name = "main") val main: WeatherMain? = null,
    @Json(name = "weather") val weatherList: List<WeatherDescription>? = null,
    @Json(name = "name") val cityName: String? = null
)

@JsonClass(generateAdapter = true)
data class WeatherMain(
    @Json(name = "temp") val temp: Double = 0.0,
    @Json(name = "temp_min") val tempMin: Double = 0.0,
    @Json(name = "temp_max") val tempMax: Double = 0.0,
    @Json(name = "humidity") val humidity: Int = 0
)

@JsonClass(generateAdapter = true)
data class WeatherDescription(
    @Json(name = "main") val main: String = "",
    @Json(name = "description") val description: String = "",
    @Json(name = "icon") val icon: String = ""
)
