package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.data.SettingsRepository
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl

const val PRACTICUM_PREFERENCES = "playlist_maker_preferences"
const val THEME_KEY = "theme_key"

class App : Application() {

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository())
    }

    private fun getSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(this)
    }

    private var darkTheme = false
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate() {
        sharedPreferences = getSharedPreferences(PRACTICUM_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPreferences.getBoolean(THEME_KEY, darkTheme)
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