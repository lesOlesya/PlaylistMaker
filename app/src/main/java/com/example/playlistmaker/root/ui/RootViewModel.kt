package com.example.playlistmaker.root.ui

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractor

class RootViewModel(private val settingsInteractor: SettingsInteractor) : ViewModel() {

    fun setAppTheme() = settingsInteractor.updateThemeSetting(settingsInteractor.getThemeSettings())
}