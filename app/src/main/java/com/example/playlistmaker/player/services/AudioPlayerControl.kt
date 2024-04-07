package com.example.playlistmaker.player.services

import com.example.playlistmaker.player.domain.util.PlayerState
import kotlinx.coroutines.flow.StateFlow

interface AudioPlayerControl {
    fun getPlayerState(): StateFlow<PlayerState>
    fun startPlayer()
    fun pausePlayer()

    fun showNotification()

    fun hideNotification()
}
