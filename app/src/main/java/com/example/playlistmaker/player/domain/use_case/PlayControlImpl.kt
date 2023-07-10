package com.example.playlistmaker.player.domain.use_case

import com.example.playlistmaker.player.data.PlayerClient
import com.example.playlistmaker.player.domain.util.TimeFormatter
import com.example.playlistmaker.player.ui.activity.PlayerState

class PlayControlImpl(private val mediaPlayer: PlayerClient) :
    PlayControl {

    private var playerState = PlayerState.PREPARED


    override fun preparePlayer(url: String) {
        mediaPlayer.preparePlayer(url)
    }

    override fun playbackControl() : PlayerState{
        when (playerState) {
            PlayerState.Plyeng -> pausePlayer()
            PlayerState.Paused, PlayerState.PREPARED -> startPlayer()
        }
        return playerState
    }

    private fun startPlayer() {
        mediaPlayer.startPlayer()
        playerState = PlayerState.Plyeng
    }

    private fun pausePlayer() {
        mediaPlayer.pausePlayer()
        playerState = PlayerState.Paused
    }

    override fun getProgressTime(): String {
        if (playerState == PlayerState.PREPARED )  return TimeFormatter.ZERO_TIME else return TimeFormatter.format(mediaPlayer.getCurrentPosition())
    }

    override fun release() {
        mediaPlayer.release()
    }


    override fun setOnStateChangeListener(callback: (PlayerState) -> Unit) {
        mediaPlayer.setOnStateChangeListener(callback)
    }
}