package com.example.playlistmaker.settings.data.impl

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.main.ui.MainActivity.Companion.THEME_KEY
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.domain.model.ThemeSettings

class SettingsRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
) : SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(sharedPreferences.getBoolean(THEME_KEY, false))
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        sharedPreferences.edit().putBoolean(THEME_KEY, settings.darkMode).apply()
    }

    override fun switchTheme(darkThemeEnabled: ThemeSettings) {
        val darkTheme = darkThemeEnabled.darkMode
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPreferences.edit().putBoolean(THEME_KEY, darkTheme).apply()
    }
}
