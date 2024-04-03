package com.example.playlistmaker.settings.data

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.domain.models.ThemeSettings

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
const val THEME_SWITCHER_KEY = "key_for_theme_switcher"

class SettingsRepositoryImpl(context: Context) : SettingsRepository {

    private var darkTheme = false
    private val sharedPreferences =
        context.getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Context.MODE_PRIVATE)

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