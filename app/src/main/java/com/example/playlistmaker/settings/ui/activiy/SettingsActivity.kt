package com.example.playlistmaker.settings.ui.activiy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.playlistmaker.App
import com.example.playlistmaker.PRACTICUM_PREFERENCES
import com.example.playlistmaker.R
import com.example.playlistmaker.THEME_KEY


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        val imageBack = findViewById<ImageView>(R.id.backToMainActivity)
        imageBack.setOnClickListener { finish() }


        val share = findViewById<TextView>(R.id.share_button)
        val support = findViewById<TextView>(R.id.support_button)
        val forward = findViewById<TextView>(R.id.forward_button)
        val themeSwitcher = findViewById<SwitchCompat>(R.id.switch_theme)
        val sharedPreferences = getSharedPreferences(PRACTICUM_PREFERENCES, MODE_PRIVATE)
        val darkTheme = sharedPreferences.getBoolean(THEME_KEY, false)
        themeSwitcher.setChecked(darkTheme)


        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message))
            intent.type = "text/plain"
            safeStartActivity(intent)
        }

        support.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.mail_to_support)))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_message))
            safeStartActivity(intent)
        }

        forward.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(getString(R.string.offer))
            safeStartActivity(intent)
        }
    }

    private fun safeStartActivity(intent: Intent) {
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(applicationContext, getString(R.string.error_toast_message), Toast.LENGTH_SHORT).show()
        }
    }
}
