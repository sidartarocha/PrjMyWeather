package com.sidarta.myweather

import android.app.Application
import com.sidarta.myweather.preferences.Preferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        initPreferences()
    }

    private fun initPreferences() {
        with(Preferences) {
            init(this@MyApplication)

            if (!this.exists("TEMPERATURE")) {
                this.set("TEMPERATURE", R.id.radioButtonC)
            }
            if (!this.exists("LANGUAGE")) {
                this.set("LANGUAGE", R.id.radioButtonEn)
            }
        }
    }


}