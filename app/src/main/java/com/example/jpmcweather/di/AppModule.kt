package com.example.jpmcweather.di

import com.example.jpmcweather.repo.WeatherRepo
import com.example.jpmcweather.repo.WeatherRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun providesWeatherRepository(): WeatherRepo{
        return WeatherRepoImpl()
    }
}