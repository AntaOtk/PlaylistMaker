package com.example.playlistmaker.player.domain

import com.example.playlistmaker.player.domain.util.PlayerState

interface PlayControl {
    fun preparePlayer(url: String)
    fun release()
    fun playbackControl(): PlayerState
    fun getProgressTime(): String

    fun setOnStateChangeListener(callback: (PlayerState) -> Unit)
}
