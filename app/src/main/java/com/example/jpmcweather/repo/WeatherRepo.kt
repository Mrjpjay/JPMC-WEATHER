package com.example.jpmcweather.repo

interface WeatherRepo {
    suspend fun getWeather(entries: String, listener: WeatherRepoImpl.WeatherListener)
}