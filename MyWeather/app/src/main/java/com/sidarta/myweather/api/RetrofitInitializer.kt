package com.sidarta.myweather.api


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInitializer {


    private val retrofitInstance = Retrofit.Builder()
        .baseUrl("http://api.openweathermap.org/data/2.5/")
        //.baseUrl("http://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getWeatherService() = retrofitInstance.create(WeatherCityService::class.java)

}