package com.example.jpmcweather.api

import com.example.jpmcweather.api.response.WeatherResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherEndPoint {

    @GET("weather")
    fun getWeather(@Query("q") cityName: String, @Query("appid") apiId: String): Call<WeatherResponse>

    @GET("{icon}@2x.png")
    fun getWeatherIcon(@Path("icon") icon: String): Call<ResponseBody>
}