package com.example.jpmcweather

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import android.Manifest
import android.location.*
import android.util.Log
import com.example.jpmcweather.api.data.WeatherData
import com.example.jpmcweather.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var progress: ProgressBar? = null
    private var textWeather: TextView? = null
    private var sharedPref: SharedPreferences? = null
    private var editor: Editor? = null
    private val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1

    private val viewModel by viewModels<WeatherViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermission()

        sharedPref = getSharedPreferences("weather", Context.MODE_PRIVATE)
        editor = sharedPref?.edit()
        val edit = findViewById<EditText>(R.id.edit)
        val btn = findViewById<Button>(R.id.btn)
        progress = findViewById(R.id.progress)
        textWeather = findViewById(R.id.txtResult)
        btn.setOnClickListener {
            viewModel.getWeather("${edit.text}")
            editor?.putString("weatherEdit", edit.text.toString())
            editor?.apply()
        }

        observers()
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCity() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {

                val geocoder = Geocoder(this@MainActivity, Locale.getDefault())
                val addresses: MutableList<Address>? =
                    geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val cityName = addresses?.get(0)?.locality
                viewModel.getWeather(cityName.toString())
                locationManager?.removeUpdates(this)
            }
        }
        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_ACCESS_FINE_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getCity()
                }
                return
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val lastEntries = sharedPref?.getString("weatherEdit", "")
        if(lastEntries?.isEmpty() == false) {
            viewModel.getWeather(lastEntries.toString())
        }
    }

    private fun observers() {
        viewModel.loadingLD.observe(this, ::loading)
        viewModel.errorLD.observe(this, ::errorMsg)
        viewModel.weatherDataLD.observe(this, ::weather)
    }

    @SuppressLint("SetTextI18n")
    private fun weather(weatherData: WeatherData) {

        val byteArray = hexToByte(weatherData)

        Glide.with(this)
            .load(byteArray)
            .into(findViewById(R.id.imageView))

        textWeather?.text = "Name: ${weatherData.name}\n" +
                "temperature: ${weatherData.temp}\n" +
                "Feels Like: ${weatherData.feelsLike}\n" +
                "Min Temp: ${weatherData.tempMin}\n" +
                "Max Temp: ${weatherData.tempMax}\n" +
                "Preassure: ${weatherData.pressure}\n" +
                "Humidity: ${weatherData.humidity}"
    }

    /**
     * Convert back from byteArray to hex for Glide to set
     */
    private fun hexToByte(weatherData: WeatherData): ByteArray {
        val byteArray = ByteArray(weatherData.icon?.length?.div(2) ?: 0)

        for (i in byteArray.indices) {
            val index = i * 2
            val j = Integer.parseInt(weatherData.icon?.substring(index, index + 2).toString(), 16)
            byteArray[i] = j.toByte()
        }
        return byteArray
    }

    private fun errorMsg(b: Boolean) {
        if (b) {
            Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loading(b: Boolean) {
        if (b) {
            progress?.visibility = View.VISIBLE
        } else {
            progress?.visibility = View.INVISIBLE
        }
    }
}