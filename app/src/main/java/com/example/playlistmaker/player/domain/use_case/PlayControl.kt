package com.example.playlistmaker.player.domain.use_case

import com.example.playlistmaker.search.domain.models.Track

interface PlayControl {
    fun playbackControl()
    fun preparePlayer(item: Track)
    fun pausePlayer()
    fun createUpdateProgressTimeRunnable(): Runnable
    fun release()
}