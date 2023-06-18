package com.example.playlistmaker.data

import android.media.MediaPlayer
import com.example.playlistmaker.domain.PlayControl
import com.example.playlistmaker.domain.PlayerInteractor
import com.example.playlistmaker.domain.models.Track
import java.util.*

class Player (private val playControl: PlayControl) : PlayerInteractor {

    private var mediaPlayer = MediaPlayer()

    private fun preparePlayer(item: Track) {
        mediaPlayer.setDataSource(item.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
                        playControl.setPlayerStatePrepared()
        }
        mediaPlayer.setOnCompletionListener {
            playControl.setPlayerState()
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