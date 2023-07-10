package com.example.playlistmaker.player.domain.use_case

import com.example.playlistmaker.player.ui.activity.PlayerState

interface PlayControl {
    fun preparePlayer(url: String)
    fun release()
    fun playbackControl(): PlayerState
    fun getProgressTime(): String

    fun setOnStateChangeListener(callback: (PlayerState) -> Unit)
}
