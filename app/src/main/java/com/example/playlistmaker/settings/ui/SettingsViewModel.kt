package com.example.playlistmaker.settings.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.models.ThemeSettings
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    application: Application,
    private val sharingInteractor: SharingInteractor
) : AndroidViewModel(application) {

    private val getSettingsInteractor by lazy { Creator.provideSettingsInteractor(getApplication()) }

    fun updateThemeSetting(checked: Boolean) {
        getSettingsInteractor.updateThemeSetting(ThemeSettings(checked))
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    companion object {
        fun getViewModelFactory(sharingInteractor: SharingInteractor): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val context =
                        this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application

                    SettingsViewModel(context, sharingInteractor)
                }
            }
    }
}