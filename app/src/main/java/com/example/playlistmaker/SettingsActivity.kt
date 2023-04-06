package com.example.playlistmaker
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*


class SettingsActivity : AppCompatActivity() {

    @SuppressLint("WrongViewCast")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        val imageBack = findViewById<ImageView>(R.id.backToMainActivity)
        imageBack.setOnClickListener{finish()}


        val share = findViewById<TextView>(R.id.share_button)
        val support = findViewById<TextView>(R.id.support_button)
        val forward = findViewById<TextView>(R.id.forward_button)

        share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message))
            intent.type  = "text/plain"
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
