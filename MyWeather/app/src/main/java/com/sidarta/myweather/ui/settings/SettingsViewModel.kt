package com.sidarta.myweather.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sidarta.myweather.preferences.Preferences

class SettingsViewModel : ViewModel() {

    var savedTemp: Int? = null
    var savedLanguage: Int? = null

    fun getSavedSettings() {
    }

    fun saveSettings(temp: Int, language: Int) {
        Preferences.set("TEMPERATURE", temp)
        Preferences.set("LANGUAGE", language)
    }
}