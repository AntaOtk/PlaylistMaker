package com.example.playlistmaker.settings.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.model.ThemeSettings
import com.example.playlistmaker.settings.util.ActionType
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {
    private val _themeSettingsState = MutableLiveData<ThemeSettings>()
    val themeSettingsState: LiveData<ThemeSettings> = _themeSettingsState

    init {
        _themeSettingsState.postValue(settingsInteractor.getThemeSettings())
    }

    fun execute(actionType: ActionType) {
        when (actionType) {
            is ActionType.Share -> sharingInteractor.shareApp()
            is ActionType.Support -> sharingInteractor.openSupport()
            is ActionType.Term -> sharingInteractor.openTerms()
            is ActionType.Theme -> updateThemeSetting(actionType.settings)
        }
    }

    private fun updateThemeSetting(settings: Boolean) {
        settingsInteractor.updateThemeSetting(ThemeSettings(settings))
        _themeSettingsState.postValue(settingsInteractor.getThemeSettings())
    }
}