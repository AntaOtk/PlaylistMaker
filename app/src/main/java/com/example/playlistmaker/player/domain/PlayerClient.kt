package com.example.playlistmaker.player.domain

import com.example.playlistmaker.player.domain.util.PlayerState

interface PlayerClient {

    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun getCurrentPosition(): Int
    fun release()
    fun setOnStateChangeListener(callback: (PlayerState) -> Unit)
    fun reset()
}
