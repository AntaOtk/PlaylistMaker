package com.example.playlistmaker.settings.ui.activiy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.util.ActionType
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel


class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory(this)
        )[SettingsViewModel::class.java]
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.switchTheme.isChecked = viewModel.getTheme()

        binding.backToMainActivity.setOnClickListener { finish() }
        binding.switchTheme.setOnCheckedChangeListener { _, checked ->
            viewModel.execute(ActionType.Theme(checked))
            (applicationContext as App).switchTheme(checked)
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
