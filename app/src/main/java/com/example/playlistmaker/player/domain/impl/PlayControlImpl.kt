package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.data.PlayerClient
import com.example.playlistmaker.player.domain.PlayControl
import com.example.playlistmaker.player.domain.util.PlayerState
import com.example.playlistmaker.player.domain.util.TimeFormatter

class PlayControlImpl(private val mediaPlayer: PlayerClient) :
    PlayControl {

    private var playerState = PlayerState.PREPARED


    override fun preparePlayer(url: String) {
        mediaPlayer.preparePlayer(url)
    }

    override fun playbackControl(): PlayerState {
        when (playerState) {
            PlayerState.PLAYING -> pausePlayer()
            PlayerState.PAUSED, PlayerState.PREPARED -> startPlayer()
        }
        return playerState
    }

    private fun startPlayer() {
        mediaPlayer.startPlayer()
        playerState = PlayerState.PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pausePlayer()
        playerState = PlayerState.PAUSED
    }

    override fun getProgressTime(): String {
        return if (playerState == PlayerState.PREPARED) TimeFormatter.ZERO_TIME else TimeFormatter.format(
            mediaPlayer.getCurrentPosition()
        )
    }

    override fun release() {
        mediaPlayer.release()
    }


    override fun setOnStateChangeListener(callback: (PlayerState) -> Unit) {
        mediaPlayer.setOnStateChangeListener { state ->
            this.playerState = state
            callback(state)
        }
    }
}