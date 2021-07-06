package com.sidarta.myweather.api

import com.sidarta.myweather.entity.FindResult
import com.sidarta.myweather.preferences.Preferences
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherCityService {

        @GET("find")
        //@GET("data/2.5/find")
        fun queryWeather(
            @Query("q")
            city:String,
            @Query("appId")
            appId:String,
            @Query("lang")
            lang:String = Preferences.getLangAPI(),
            @Query("units")
            unit: String = Preferences.getUnitsAPI()
        ): Call<QueryResult>


        @GET("group")
        fun findByFavorite(
            @Query("id")
            idCity:String,
            @Query("appId")
            appId: String,
            @Query("lang")
            lang:String = Preferences.getLangAPI(),
            @Query("units")
            unit: String = Preferences.getUnitsAPI()
       ): Call<QueryResult>

    }
