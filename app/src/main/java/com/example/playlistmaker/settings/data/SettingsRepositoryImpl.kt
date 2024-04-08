package com.example.playlistmaker.settings.data

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.domain.models.ThemeSettings

const val THEME_SWITCHER_KEY = "key_for_theme_switcher"

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    SettingsRepository {

    private var darkTheme = false

    override fun getThemeSettings(): ThemeSettings {
        darkTheme =
            sharedPreferences.getString(THEME_SWITCHER_KEY, darkTheme.toString()).toBoolean()
        return ThemeSettings(darkTheme)
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        switchTheme(settings.darkTheme)
        sharedPreferences.edit()
            .putString(THEME_SWITCHER_KEY, settings.darkTheme.toString())
            .apply()
    }

    private fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}