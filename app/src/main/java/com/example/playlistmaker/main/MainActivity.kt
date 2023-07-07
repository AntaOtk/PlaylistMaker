package com.example.playlistmaker.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.libraby.LibraryActivity
import com.example.playlistmaker.search.ui.activity.SearchActivity
import com.example.playlistmaker.settings.ui.activiy.SettingsActivity


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search_button)
        val libraryButton = findViewById<Button>(R.id.library_button)
        val settigsButton = findViewById<Button>(R.id.settings_button)

        searchButton.setOnClickListener {
            SearchActivity.startActivity(this)
        }

        libraryButton.setOnClickListener {
            val displayIntent = Intent(this, LibraryActivity::class.java)
            startActivity(displayIntent)
        }

        val buttonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val displayIntent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(displayIntent)
            }
        }

        settigsButton.setOnClickListener(buttonClickListener)
    }
}