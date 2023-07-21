package com.example.playlistmaker.settings.ui.activiy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.example.playlistmaker.settings.util.ActionType
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsActivity : AppCompatActivity() {

    private val viewModel by viewModel<SettingsViewModel>()
    private lateinit var binding: ActivitySettingsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.switchTheme.isChecked = viewModel.getTheme()
        binding.backToMainActivity.setOnClickListener { finish() }
        binding.switchTheme.setOnCheckedChangeListener { _, checked ->
            viewModel.execute(ActionType.Theme(checked))
        }
        binding.shareButton.setOnClickListener {
            viewModel.execute(ActionType.Share)
        }
        binding.supportButton.setOnClickListener {
            viewModel.execute(ActionType.Support)
        }
        binding.forwardButton.setOnClickListener {
            viewModel.execute(ActionType.Term)
        }
    }
}
