package com.example.playlistmaker.player.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.AudioPlayerBinding
import com.example.playlistmaker.player.domain.util.PlayerState
import com.example.playlistmaker.player.ui.viewmodel.PlayerViewModel
import com.example.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayer : AppCompatActivity() {
    companion object {
        fun startActivity(context: Context, track: Track) {
            val intent = Intent(context, AudioPlayer::class.java)
            intent.putExtra("TRACK", track)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: AudioPlayerBinding
    private var mainThreadHandler: Handler? = null
    private lateinit var viewModel: PlayerViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_player)
        binding = AudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val track: Track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("TRACK",Track::class.java)!!
        } else {
            intent.getParcelableExtra("TRACK")!!
        }
        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getViewModelFactory()
        )[PlayerViewModel::class.java]
        mainThreadHandler = Handler(Looper.getMainLooper())
        binding.playButton.setOnClickListener { viewModel.playbackControl() }

        viewModel.observeState().observe(this) {
            render(it)
        }
        viewModel.observeProgressTimeState().observe(this) {
            progressTimeViewUpdate(it)
        }

        binding.backButton.setOnClickListener { finish() }



        binding.title.text = track.trackName
        binding.artist.text = track.artistName
        binding.albumName.text = track.collectionName
        binding.year.text = track.releaseDate.substring(0, 4)
        binding.styleName.text = track.primaryGenreName
        binding.countryName.text = track.country
        binding.trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        Glide.with(applicationContext)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(applicationContext.resources.getDimensionPixelSize(R.dimen.audioplayer_corner_radius_art)))
            .into(binding.cover)

        viewModel.prepare(track.previewUrl)
    }

    private fun render(state: PlayerState) {
        when (state) {
            PlayerState.PLAYING -> startPlayer()
            PlayerState.PAUSED, PlayerState.PREPARED -> pausePlayer()
        }
    }

    private fun startPlayer() {
        binding.playButton.setImageResource(R.drawable.pause_button)
    }

    private fun pausePlayer() {
        binding.playButton.setImageResource(R.drawable.play_button)
    }

    private fun progressTimeViewUpdate(progressTime: String) {
        binding.progressTime.text = progressTime
    }
}
