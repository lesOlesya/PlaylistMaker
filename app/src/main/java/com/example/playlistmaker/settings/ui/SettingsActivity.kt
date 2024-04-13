package com.example.playlistmaker.settings.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingsToolbar.setNavigationOnClickListener {
            finish()
        }

        val themeSwitcher = binding.themeSwitcher

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.updateThemeSetting(checked)
        }
        themeSwitcher.isChecked

        binding.flShareApp.setOnClickListener {
            viewModel.shareApp()
        }

        binding.flSupport.setOnClickListener {
            viewModel.openSupport()
        }

        binding.flTermsOfUse.setOnClickListener {
            viewModel.openTerms()
        }
    }

}