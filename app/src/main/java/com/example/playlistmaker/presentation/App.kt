package com.example.playlistmaker.presentation

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val PRACTICUM_PREFERENCES = "playlist_maker_preferences"
const val THEME_KEY = "theme_key"

class App  : Application() {

    var darkTheme = false
    private lateinit var sharedPreferences : SharedPreferences


    override fun onCreate() {

        sharedPreferences = getSharedPreferences(PRACTICUM_PREFERENCES,MODE_PRIVATE)
        darkTheme = sharedPreferences.getBoolean(THEME_KEY,darkTheme)
        super.onCreate()
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPreferences.edit().putBoolean(THEME_KEY, darkTheme).apply()
    }
}