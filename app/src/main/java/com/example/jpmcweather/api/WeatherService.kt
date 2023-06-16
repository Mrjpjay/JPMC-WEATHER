package com.example.jpmcweather.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherService {

    private lateinit var retrofit: Retrofit
    private lateinit var retrofit2: Retrofit

    val instance: WeatherEndPoint by lazy {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(WeatherEndPoint::class.java)
    }

    val iconInstance: WeatherEndPoint by lazy {
        retrofit2 = Retrofit.Builder()
            .baseUrl(BASE_URL_ICON)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit2.create(WeatherEndPoint::class.java)
    }
}