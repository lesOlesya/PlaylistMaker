package com.example.playlistmaker.settings.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    private val getSharingInteractor by lazy { Creator.provideSharingInteractor(this)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory(getSharingInteractor)
        )[SettingsViewModel::class.java]

        binding.settingsBack.setOnClickListener {
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