package com.example.playlistmaker.domain

import android.media.MediaPlayer
import com.example.playlistmaker.R
import com.example.playlistmaker.data.Player
import com.example.playlistmaker.presentation.AudioPlayer
import java.text.SimpleDateFormat
import java.util.*


class PlayControl(val mediaPlayer: PlayerInteractor, val playerPresenter: PlayerPresenter) {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private var playerState = STATE_DEFAULT


    fun setPlayerState() {
        playerState = STATE_PREPARED

    }

    fun setPlayerStatePrepared() {
        playerState = STATE_PREPARED
        playerPresenter.playButtonEnabled()
    }

    fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    fun startPlayer() {
        mediaPlayer.startPlayer()
        playerPresenter.startPlayer()
        playerState = STATE_PLAYING
    }

    fun pausePlayer() {
        mediaPlayer.pausePlayer()
        playerPresenter.pausePlayer()
        playerState = STATE_PAUSED
    }

    fun createUpdateProgressTimeRunnable(): Runnable {
        return object : Runnable {
            override fun run() {

                when (playerState) {
                    STATE_PLAYING -> {
                        playerPresenter.progressTimeViewUpdate(TimeFormatter.format(mediaPlayer.getCurrentPosition()))
                        playerPresenter.postDelayed(this)
                    }
                    STATE_PAUSED -> {
                        playerPresenter.removeCallbacks(this)
                    }
                    STATE_PREPARED -> {
                        playerPresenter.pausePlayer()
                        playerPresenter.progressTimeViewUpdate(TimeFormatter.ZERO_TIME)
                        playerPresenter.removeCallbacks(this)
                    }
                }
            }
        }
    }


        fun release() {
            mediaPlayer.release()
        }
    }