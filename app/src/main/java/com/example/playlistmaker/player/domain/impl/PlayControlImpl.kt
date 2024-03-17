package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.PlayControl
import com.example.playlistmaker.player.domain.PlayerClient
import com.example.playlistmaker.player.domain.util.PlayerState
import com.example.playlistmaker.player.domain.util.TimeFormatter

class PlayControlImpl(private val mediaPlayer: PlayerClient, private var playerState: PlayerState) :
    PlayControl {

    override fun preparePlayer(url: String) {
        mediaPlayer.preparePlayer(url)
    }

    override fun playbackControl(): PlayerState {
        when (playerState) {
            PlayerState.PLAYING -> pausePlayer()
            PlayerState.PAUSED, PlayerState.INIT, PlayerState.COMPLETED-> startPlayer()
        }
        return playerState
    }

    private fun startPlayer() {
        mediaPlayer.startPlayer()
        playerState = PlayerState.PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pausePlayer()
        playerState = PlayerState.PAUSED
    }

    override fun reset() {
        mediaPlayer.reset()
    }

    override fun getProgressTime(): String {
        return if (playerState == PlayerState.COMPLETED) TimeFormatter.ZERO_TIME else TimeFormatter.format(
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