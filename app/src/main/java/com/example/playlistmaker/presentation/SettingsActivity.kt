package com.example.playlistmaker.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.example.playlistmaker.App
import com.example.playlistmaker.PRACTICUM_PREFERENCES
import com.example.playlistmaker.R
import com.example.playlistmaker.THEME_KEY


class SettingsActivity : AppCompatActivity() {

    @SuppressLint("WrongViewCast")


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
        var darkTheme = sharedPreferences.getBoolean(THEME_KEY, false)
        themeSwitcher.setChecked(darkTheme)


        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message))
            intent.type = "text/plain"
            startActivity(intent)
        }

        support.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(R.string.mail_to_support))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_message))
            startActivity(intent)
        }

        forward.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(getString(R.string.offer))
            startActivity(intent)
        }

    }

}
