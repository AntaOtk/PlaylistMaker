package com.example.playlistmaker.domain.use_case

import com.example.playlistmaker.domain.models.Track

interface PlayControl {
    fun playbackControl()
    fun preparePlayer(item: Track)
    fun pausePlayer()
    fun createUpdateProgressTimeRunnable(): Runnable
    fun release()
}
