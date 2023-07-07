package com.example.playlistmaker.settings.ui.activiy

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.ui.util.ActionType
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel


class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]


        val imageBack = findViewById<ImageView>(R.id.backToMainActivity)
        imageBack.setOnClickListener { finish() }

        val share = findViewById<TextView>(R.id.share_button)
        val support = findViewById<TextView>(R.id.support_button)
        val forward = findViewById<TextView>(R.id.forward_button)
        val themeSwitcher = findViewById<SwitchCompat>(R.id.switch_theme)

        themeSwitcher.setChecked(viewModel.getTheme())


        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.execute(ActionType.Theme(checked))
            (applicationContext as App).switchTheme(checked)
        }

        share.setOnClickListener {
            val intent = viewModel.execute(ActionType.Share)
        }

        support.setOnClickListener {
            viewModel.execute(ActionType.Support)
        }

        forward.setOnClickListener {
            viewModel.execute(ActionType.Term)
        }
    }

    private fun safeStartActivity(intent: Intent) {
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                applicationContext,
                getString(R.string.error_toast_message),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
