package com.example.playlistmaker.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.PlayerPresenter
import com.example.playlistmaker.domain.use_case.PlayControl
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayer : AppCompatActivity(), PlayerPresenter {
    companion object {
        private const val DELAY_MILLIS = 25L

        fun startActivity(context: Context) {
            val intent = Intent(context, AudioPlayer::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var playButton: ImageButton
    private lateinit var progressTimeView: TextView
    private var mainThreadHandler: Handler? = null
    private lateinit var playControl: PlayControl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_player)
        playControl = Creator.createPlayControl(this)

        playButton = findViewById(R.id.playButton)
        progressTimeView = findViewById(R.id.progressTime)

        val name = findViewById<TextView>(R.id.title)
        val artist = findViewById<TextView>(R.id.artist)
        val duration = findViewById<TextView>(R.id.trackTime)
        val album = findViewById<TextView>(R.id.albumName)
        val year = findViewById<TextView>(R.id.year)
        val ganre = findViewById<TextView>(R.id.styleName)
        val country = findViewById<TextView>(R.id.countryName)
        val artwork = findViewById<ImageView>(R.id.cover)
        val item = Creator.getOneTrackRepository(this).getTrack()

        mainThreadHandler = Handler(Looper.getMainLooper())
        playButton.setOnClickListener { playControl.playbackControl() }

        val imageBack = findViewById<ImageView>(R.id.backButton)
        imageBack.setOnClickListener { finish() }

        name.text = item.trackName
        artist.text = item.artistName
        album.text = item.collectionName
        year.text = item.releaseDate.substring(0, 4)
        ganre.text = item.primaryGenreName
        country.text = item.country
        duration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)

        playControl.preparePlayer(item)

        Glide.with(applicationContext)
            .load(item.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(applicationContext.resources.getDimensionPixelSize(R.dimen.audioplayer_corner_radius_art)))
            .into(artwork)
    }

    override fun startPlayer() {
        playButton.setImageResource(R.drawable.pause_button)
        mainThreadHandler?.post(
            playControl.createUpdateProgressTimeRunnable()
        )
    }

    override fun pausePlayer() {
        playButton.setImageResource(R.drawable.play_button)
    }

    override fun progressTimeViewUpdate(progressTime: String) {
        progressTimeView.text = progressTime
    }

    override fun playButtonEnabled() {
        playButton.isEnabled = true
    }

    override fun postDelayed(runnable: Runnable) {
        mainThreadHandler?.postDelayed(runnable, DELAY_MILLIS)
    }

    override fun removeCallbacks(runnable: Runnable) {
        mainThreadHandler?.removeCallbacks(runnable)
    }


    override fun onPause() {
        super.onPause()
        playControl.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        playControl.release()
    }
}
