package com.example.playlistmaker.domain

interface PlayerPresenter {
    fun startPlayer()
    fun pausePlayer()
    fun progressTimeViewUpdate(progressTime: String)
    fun playButtonEnabled()
    fun postDelayed(runnable: Runnable)
    fun removeCallbacks(runnable: Runnable)
}