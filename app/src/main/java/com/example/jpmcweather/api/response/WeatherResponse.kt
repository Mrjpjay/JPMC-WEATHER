package com.example.jpmcweather.api.response

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("weather") val list:List<Weather>,
    @SerializedName("main") val main: Main,
    @SerializedName("name") val name: String
)
data class Main(
    @SerializedName("temp") val temp: Float,
    @SerializedName("feels_like") val feelsLike: Float,
    @SerializedName("temp_min") val tempMin: Float,
    @SerializedName("temp_max") val tempMax: Float,
    @SerializedName("pressure") val pressure: Float,
    @SerializedName("humidity") val humidity: Int

)

data class Weather(
    @SerializedName("id") val id: Int,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)
