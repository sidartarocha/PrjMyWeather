package com.sidarta.myweather

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.sidarta.myweather.common.ContextWrapper
import com.sidarta.myweather.preferences.Preferences
import java.util.Locale


class BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        val locale = Locale(Preferences.getLanguage(), Preferences.getCountry())

        super.attachBaseContext(ContextWrapper.wrap(newBase, locale))
    }
}


