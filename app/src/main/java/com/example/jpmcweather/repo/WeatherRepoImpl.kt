package com.example.jpmcweather.repo

import android.util.Log
import com.example.jpmcweather.api.APP_ID
import com.example.jpmcweather.api.WeatherService
import com.example.jpmcweather.api.data.WeatherData
import com.example.jpmcweather.api.response.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepoImpl : WeatherRepo {

    override suspend fun getWeather(entries: String, listener: WeatherListener) =
        withContext(Dispatchers.IO) {
            responseHandler(entries, listener)
        }

    private fun responseHandler(
        city: String,
        listener: WeatherListener
    ) {
        try {
            val call = WeatherService().instance.getWeather(city, APP_ID)
            val response = call.execute()
            if (response.isSuccessful) {
                mapResponse(response.body(), listener)
            } else {
                listener.onError("${response.body()}")
            }
        } catch (e: Exception) {
            Log.i("WeatherRepoImpl", "$e")
            listener.onError("$e")
        }
    }

    private fun mapResponse(
        response: WeatherResponse?,
        listener: WeatherListener
    ) {

        val temp = response?.main?.temp
        val feelsLike = response?.main?.feelsLike
        val tempMin = response?.main?.tempMin
        val tempMax = response?.main?.tempMax
        val pressure = response?.main?.pressure
        val humidity = response?.main?.humidity
        val name = response?.name
        val icon = response?.list?.get(0)?.icon

        //get the icon image
        val hexString = byteArrayToHex(icon)

        listener.onSuccess(
            WeatherData(
                hexString,
                name,
                temp,
                feelsLike,
                tempMin,
                tempMax,
                pressure,
                humidity
            )
        )
    }

    /**
     * Convert Icon byteArray into Hex String
     */
    private fun byteArrayToHex(icon: String?): String? {
        val iconInputStream =
            WeatherService().iconInstance.getWeatherIcon("$icon").execute().body()?.byteStream()
        val byteArray = iconInputStream?.readBytes()
        return byteArray?.joinToString("") { "%02x".format(it) }
    }

    fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }

    interface WeatherListener {
        fun onSuccess(weatherData: WeatherData)
        fun onError(error: String)
    }
}