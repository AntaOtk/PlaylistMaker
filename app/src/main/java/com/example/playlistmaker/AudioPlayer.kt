package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.searchlist.Track
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayer : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 25L
    }

    private var playerState = STATE_DEFAULT
    private lateinit var playButton: ImageButton
    private lateinit var progressTimeView: TextView
    private var mediaPlayer = MediaPlayer()
    private var mainThreadHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_player)
        val searchHistory = SearchHistory(this)
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
        mainThreadHandler = Handler(Looper.getMainLooper())
        val item = searchHistory.read().get(0)



        preparePlayer(item)

        playButton.setOnClickListener {
            playbackControl()
        }


        val imageBack = findViewById<ImageView>(R.id.backButton)
        imageBack.setOnClickListener { finish() }




        name.text = item.trackName
        artist.text = item.artistName
        album.text = item.collectionName
        year.text = item.releaseDate.substring(0, 4)
        ganre.text = item.primaryGenreName
        country.text = item.country
        duration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)


        Glide.with(applicationContext)
            .load(item.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(applicationContext.resources.getDimensionPixelSize(R.dimen.audioplayer_corner_radius_art)))
            .into(artwork)


    }

    private fun preparePlayer(item: Track) {
        mediaPlayer.setDataSource(item.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED

        }
    }


    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.pause_button)
        playerState = STATE_PLAYING
        mainThreadHandler?.post(
            createUpdateProgressTime()
        )
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.play_button)
        playerState = STATE_PAUSED
    }

    private fun createUpdateProgressTime(): Runnable {
        return object : Runnable {
            override fun run() {
                when (playerState) {
                    STATE_PLAYING -> {
                        progressTimeView.text = SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(mediaPlayer.currentPosition)
                        mainThreadHandler?.postDelayed(this, DELAY)
                    }
                    STATE_PAUSED -> {
                        mainThreadHandler?.removeCallbacks(this)
                    }
                    STATE_PREPARED -> {
                        mainThreadHandler?.removeCallbacks(this)
                        playButton.setImageResource(R.drawable.play_button)
                        progressTimeView.text = "00:00"
                    }

                }
            }
        }

    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }


    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }


}






