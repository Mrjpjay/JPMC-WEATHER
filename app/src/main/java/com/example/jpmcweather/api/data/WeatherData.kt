package com.example.jpmcweather.api.data

data class WeatherData(
    val icon: String?,
    val name: String?,
    val temp: Float?,
    val feelsLike: Float?,
    val tempMin: Float?,
    val tempMax: Float?,
    val pressure: Float?,
    val humidity: Int?
)
