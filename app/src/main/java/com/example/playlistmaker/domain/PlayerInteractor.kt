package com.example.playlistmaker.domain

import com.example.playlistmaker.R

interface PlayerInteractor {
    fun startPlayer()
    fun pausePlayer()
    fun getCurrentPosition(): Int
    fun release()
}