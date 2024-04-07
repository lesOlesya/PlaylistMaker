package com.example.playlistmaker.main.ui

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractor

class MainViewModel(private val settingsInteractor: SettingsInteractor) : ViewModel() {

    fun setAppTheme() = settingsInteractor.updateThemeSetting(settingsInteractor.getThemeSettings())
}