package com.example.playlistmaker.util

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.presentation.ui.search.view_model.TracksSearchPresenter
import com.example.playlistmaker.presentation.ui.settings.activity.PLAYLIST_MAKER_PREFERENCES
import com.example.playlistmaker.presentation.ui.settings.activity.THEME_SWITCHER_KEY

class TracksApplication : Application() {

    var tracksSearchPresenter : TracksSearchPresenter? = null

    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)

        darkTheme = sharedPrefs.getString(THEME_SWITCHER_KEY, darkTheme.toString()).toBoolean()
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
    }
}