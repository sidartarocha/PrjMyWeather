package com.sidarta.myweather.preferences

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.sidarta.myweather.R
import java.lang.reflect.Type

object Preferences {

    private const val SHARED_PREF_NAME = "WEATHER_PREFERENCES"
    private lateinit var shp: SharedPreferences
    private val gson = Gson()

    fun init(context: Context) {
        shp =
            context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    fun <T> get(key: String, type: Type): T {
        val stringValue = shp.getString(key, null) ?: throw Exception()
        return gson.fromJson(stringValue, type) ?: throw Exception()
    }

    fun set(key: String, value: Any?) {
        value?.let {
            shp.edit().putString(key, gson.toJson(it)).apply()
        } ?: shp.edit().remove(key).apply()
    }

    fun exists(key: String): Boolean {
        val stringValue = shp.getString(key, null)
        return !stringValue.isNullOrBlank()
    }

    fun getLanguage(): String {
        return when (get<Int>("LANGUAGE", Int::class.java)) {
            R.id.radioButtonEn -> "en"
            R.id.radioButtonPt -> "pt"
            else -> "ERROR"
        }
    }

    fun getLanguageId(): Int {
        return get("LANGUAGE", Int::class.java)
    }

    fun getCountry(): String {
        return when (get<Int>("LANGUAGE", Int::class.java)) {
            R.id.radioButtonEn -> "US"
            R.id.radioButtonPt -> "BR"
            else -> "ERROR"
        }
    }

    fun getLangAPI(): String {
        return when (get<Int>("LANGUAGE", Int::class.java)) {
            R.id.radioButtonEn -> "en"
            R.id.radioButtonPt -> "pt_br"
            else -> "ERROR"
        }
    }

    fun getUnitsAPI(): String {
        return when (get<Int>("TEMPERATURE", Int::class.java)) {
            R.id.radioButtonK -> "standard"
            R.id.radioButtonF -> "imperial"
            R.id.radioButtonC -> "metric"
            else -> "ERROR"
        }
    }

    fun getTemperature(): String {
        return when (get<Int>("TEMPERATURE", Int::class.java)) {
            R.id.radioButtonK -> "ºK"
            R.id.radioButtonF -> "ºF"
            R.id.radioButtonC -> "ºC"
            else -> "ERROR"
        }
    }

    fun getTemperatureId(): Int {
        return get("TEMPERATURE", Int::class.java)
    }

    fun clear() {
        shp.edit().clear().apply()
    }


}