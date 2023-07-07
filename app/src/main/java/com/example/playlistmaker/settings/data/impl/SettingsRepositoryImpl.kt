package com.example.playlistmaker.settings.data.impl

import android.content.Context
import com.example.playlistmaker.settings.data.SettingsRepository
import com.example.playlistmaker.settings.domain.model.ThemeSettings

class SettingsRepositoryImpl(context: Context) : SettingsRepository {

    companion object {

        const val PRACTICUM_PREFERENCES = "playlist_maker_preferences"
        const val THEME_KEY = "theme_key"
    }

    private val sharedPreferences = context.getSharedPreferences(
        PRACTICUM_PREFERENCES,
        Context.MODE_PRIVATE
    )

    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(sharedPreferences.getBoolean(THEME_KEY, false))
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        sharedPreferences.edit().putBoolean(THEME_KEY, settings.darkMode).apply()
    }
}