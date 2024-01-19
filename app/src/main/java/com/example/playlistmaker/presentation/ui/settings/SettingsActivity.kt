package com.example.playlistmaker.presentation.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
const val THEME_SWITCHER_KEY = "key_for_theme_switcher"

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)

        binding.settingsBack.setOnClickListener {
            finish()
        }

        val themeSwitcher = binding.themeSwitcher
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            sharedPrefs.edit()
                .putString(THEME_SWITCHER_KEY, checked.toString())
                .apply()
        }
        themeSwitcher.isChecked

        binding.flShareApp.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.link_YP_android))
            startActivity(Intent.createChooser(intent, null))
        }

        binding.flSupport.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_YP))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.message_YP))
                startActivity(this)
            }
        }

        binding.flTermsOfUse.setOnClickListener {
            val url = Uri.parse(getString(R.string.link_YP_terms_of_use))
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }
    }

}