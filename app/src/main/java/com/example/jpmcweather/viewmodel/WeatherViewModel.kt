package com.example.jpmcweather.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jpmcweather.api.data.WeatherData
import com.example.jpmcweather.repo.WeatherRepo
import com.example.jpmcweather.repo.WeatherRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repo: WeatherRepo
):ViewModel(){

    private val weatherDataMLD = MutableLiveData<WeatherData>()
    val weatherDataLD: LiveData<WeatherData> = weatherDataMLD

    private val loadingMLD = MutableLiveData<Boolean>()
    val loadingLD: LiveData<Boolean> = loadingMLD

    private val errorMLD = MutableLiveData<Boolean>()
    val errorLD: LiveData<Boolean> = errorMLD

    fun getWeather(cityNameZipAndCountry: String){
        loadingMLD.value = true
        viewModelScope.launch {
            repo.getWeather(cityNameZipAndCountry, object :WeatherRepoImpl.WeatherListener{
                override fun onSuccess(weatherData: WeatherData) {
                    loadingMLD.postValue(false)
                    errorMLD.postValue(false)
                    weatherDataMLD.postValue(weatherData)
                }

                override fun onError(error: String) {
                    errorMLD.postValue(true)
                    loadingMLD.postValue(false)
                }
            })
        }
    }
}