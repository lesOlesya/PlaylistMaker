package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import com.google.android.material.switchmaterial.SwitchMaterial

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
const val THEME_SWITCHER_KEY = "key_for_theme_switcher"

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)

        val backButton = findViewById<ImageButton>(R.id.settings_back)
        backButton.setOnClickListener {
            finish()
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            sharedPrefs.edit()
                .putString(THEME_SWITCHER_KEY, checked.toString())
                .apply()
        }
        themeSwitcher.isChecked

        val shareButton = findViewById<FrameLayout>(R.id.flShareApp)
        shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.link_YP_android))
            startActivity(Intent.createChooser(intent, null))
        }

        val supportButton = findViewById<FrameLayout>(R.id.flSupport)
        supportButton.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_YP))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.message_YP))
                startActivity(this)
            }
        }

        val termsOfUseButton = findViewById<FrameLayout>(R.id.flTermsOfUse)
        termsOfUseButton.setOnClickListener {
            val url = Uri.parse(getString(R.string.link_YP_terms_of_use))
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }
    }

}