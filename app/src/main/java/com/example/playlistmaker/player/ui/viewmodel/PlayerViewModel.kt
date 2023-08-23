package com.example.playlistmaker.player.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.PlayControl
import com.example.playlistmaker.player.domain.util.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(private val playerInteractor: PlayControl) :
    ViewModel() {
    companion object {
        private const val DELAY_MILLIS = 300L
    }

    init {
        playerInteractor.setOnStateChangeListener { state ->
            stateLiveData.postValue(state)
            val progressTime = playerInteractor.getProgressTime()
            stateProgressTimeLiveData.postValue(progressTime)
            if (state == PlayerState.PREPARED) timerJob?.cancel()
        }
    }

    private val stateLiveData = MutableLiveData(PlayerState.PREPARED)
    fun observeState(): LiveData<PlayerState> = stateLiveData

    private val stateProgressTimeLiveData = MutableLiveData<String>()
    fun observeProgressTimeState(): LiveData<String> = stateProgressTimeLiveData

    private var timerJob: Job? = null

    private fun startTimer(state: PlayerState) {
        timerJob = viewModelScope.launch {
            while (state == PlayerState.PLAYING){
                delay(DELAY_MILLIS)
                stateProgressTimeLiveData.postValue( playerInteractor.getProgressTime())
            }
        }
    }

   /* private val progressTimeRunnable = object : Runnable {
        override fun run() {
            val progressTime = playerInteractor.getProgressTime()
            stateProgressTimeLiveData.postValue(progressTime)
            mainThreadHandler.postDelayed(this, DELAY_MILLIS)
        }
    } */

    fun prepare(url: String) {
        playerInteractor.preparePlayer(url)
    }

    fun playbackControl() {
        val state = playerInteractor.playbackControl()
        renderState(state)
        if (state == PlayerState.PLAYING) startTimer(state) else timerJob?.cancel()

    }


    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
    }

    fun onPause() {
        playerInteractor.pausePlayer()
        renderState(PlayerState.PAUSED)
    }

    private fun renderState(state: PlayerState) {
        stateLiveData.postValue(state)
    }
}
