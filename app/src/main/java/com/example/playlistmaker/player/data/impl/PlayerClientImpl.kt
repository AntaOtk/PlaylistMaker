package com.example.playlistmaker.player.data.impl

import android.media.MediaPlayer
import com.example.playlistmaker.player.data.PlayerClient
import com.example.playlistmaker.player.domain.util.PlayerState

class PlayerClientImpl : PlayerClient {

    private var mediaPlayer = MediaPlayer()
    private var stateCallback: ((PlayerState) -> Unit)? = null

    override fun preparePlayer (url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            stateCallback?.invoke(PlayerState.PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            stateCallback?.invoke(PlayerState.PREPARED)
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
    override fun setOnStateChangeListener(callback: (PlayerState) -> Unit) {
        stateCallback = callback
    }
}