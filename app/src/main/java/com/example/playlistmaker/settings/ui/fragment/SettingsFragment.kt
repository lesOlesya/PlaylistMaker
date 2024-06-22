package com.example.playlistmaker.settings.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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