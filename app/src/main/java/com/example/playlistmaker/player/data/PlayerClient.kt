package com.example.playlistmaker.player.data

import com.example.playlistmaker.player.ui.activity.PlayerState
import com.example.playlistmaker.search.domain.model.Track

interface PlayerClient {

    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun getCurrentPosition(): Int
    fun release()
    fun setOnStateChangeListener(callback: (PlayerState) -> Unit)
}
