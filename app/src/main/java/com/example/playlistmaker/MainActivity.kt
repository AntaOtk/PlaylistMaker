package com.example.playlistmaker

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val find_button = findViewById<Button>(R.id.find_button)
        val library_button = findViewById<Button>(R.id.library_button)
        val settigs_button = findViewById<Button>(R.id.settigs_button)

        find_button.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на кнопку Поиск!", Toast.LENGTH_SHORT).show()
        }

        library_button.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на кнопку Медиатека!", Toast.LENGTH_SHORT).show()
        }

        val buttonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Нажали на кнопку!", Toast.LENGTH_SHORT).show()
            }
        }

        settigs_button.setOnClickListener(buttonClickListener)

    }
}