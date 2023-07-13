package com.example.playlistmaker.player.ui.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.PlayControl
import com.example.playlistmaker.player.domain.util.PlayerState

class PlayerViewModel(private val playerInteractor: PlayControl) :
    ViewModel() {
    companion object {
        private const val DELAY_MILLIS = 25L
    }
    init {
        playerInteractor.setOnStateChangeListener { state ->
            stateLiveData.postValue(state)
            val progressTime = playerInteractor.getProgressTime()
            stateProgressTimeLiveData.postValue(progressTime)
            if (state == PlayerState.PREPARED) mainThreadHandler.removeCallbacks(
                progressTimeRunnable
            )
        }
    }

    private val stateLiveData = MutableLiveData(PlayerState.PREPARED)
    fun observeState(): LiveData<PlayerState> = stateLiveData

    private val stateProgressTimeLiveData = MutableLiveData<String>()
    fun observeProgressTimeState(): LiveData<String> = stateProgressTimeLiveData


    private var mainThreadHandler = Handler(Looper.getMainLooper())
    private val progressTimeRunnable = object : Runnable {
        override fun run() {
            val progressTime = playerInteractor.getProgressTime()
            stateProgressTimeLiveData.postValue(progressTime)
            mainThreadHandler.postDelayed(this, DELAY_MILLIS)
        }
    }

    fun prepare(url: String) {
        playerInteractor.preparePlayer(url)
    }

    fun playbackControl() {
        val state = playerInteractor.playbackControl()
        renderState(state)
        if (state == PlayerState.PLAYING) mainThreadHandler.post(progressTimeRunnable) else mainThreadHandler.removeCallbacks(
            progressTimeRunnable
        )
    }


    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
        mainThreadHandler.removeCallbacks(progressTimeRunnable)
    }

    fun onPause() {
        playerInteractor.pausePlayer()
        renderState(PlayerState.PAUSED)
    }

    private fun renderState(state: PlayerState) {
        stateLiveData.postValue(state)
    }
}
