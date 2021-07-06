package com.sidarta.myweather.api

import com.sidarta.myweather.entity.Sys
import com.sidarta.myweather.entity.Wind

data class QueryResult(val message:String, val cod:String, val list:List<Place>) {

}

data class Place (
    val id:String,
    val name:String,
    val main:Main,//Temperatura,
    val weather:List<Weather>,
    val sys: Sys,
    val wind: Wind
) {

}

data class Main(
    val temp :Float,
    val humidity:Int,
    val pressure:Int
)

data class Sys(
    val country:String
)

data class Wind(
    val speed:Float
)

data class Weather(
    val icon:String,
    val description: String
)


