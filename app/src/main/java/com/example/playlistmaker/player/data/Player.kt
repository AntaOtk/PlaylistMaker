package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.search.domain.model.Track

class Player : PlayerInteractor {

    private var mediaPlayer = MediaPlayer()

    override fun preparePlayer(
        track: Track,
        prepareCallback: () -> Unit,
        completeCallback: () -> Unit
    ) {

        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            prepareCallback()
        }
        mediaPlayer.setOnCompletionListener {
            completeCallback()
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun release() {
        mediaPlayer.release()
    }
}