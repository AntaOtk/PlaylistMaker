package com.example.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.FavoriteTracksInteractor
import com.example.playlistmaker.player.domain.PlayControl
import com.example.playlistmaker.player.domain.util.PlayerState
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayControl,
    private val favoriteInteractor: FavoriteTracksInteractor
) :
    ViewModel() {
    companion object {
        private const val DELAY_MILLIS = 300L
    }

    init {
        playerInteractor.setOnStateChangeListener { state ->
            stateLiveData.postValue(state)
            val progressTime = playerInteractor.getProgressTime()
            stateProgressTimeLiveData.postValue(progressTime)
            if (state == PlayerState.PREPARED) cancelTimer()
        }
    }

    private val stateLiveData = MutableLiveData(PlayerState.PREPARED)
    fun observeState(): LiveData<PlayerState> = stateLiveData

    private val stateProgressTimeLiveData = MutableLiveData<String>()
    fun observeProgressTimeState(): LiveData<String> = stateProgressTimeLiveData

    private val stateFavoriteData = MutableLiveData<Boolean>()
    fun observeFavoriteState(): LiveData<Boolean> = stateFavoriteData

    private var timerJob: Job? = null

    private fun startTimer(state: PlayerState) {
        timerJob = viewModelScope.launch(Dispatchers.Default) {
            while (state == PlayerState.PLAYING) {
                delay(DELAY_MILLIS)
                stateProgressTimeLiveData.postValue(playerInteractor.getProgressTime())
            }
        }
    }

    fun prepare(track: Track) {
        viewModelScope.launch {
            playerInteractor.preparePlayer(track.previewUrl)
            getChecked(track)
        }
    }

    fun playbackControl() {
        val state = playerInteractor.playbackControl()
        renderState(state)
        if (state == PlayerState.PLAYING) startTimer(state) else cancelTimer()

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

    private fun cancelTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch {
            renderFavoriteState(favoriteInteractor.updateFavorite(track))
        }
    }

    fun getChecked(track: Track) {
        viewModelScope.launch {
            renderFavoriteState(favoriteInteractor.getChecked(track.trackId))
        }
    }

    private fun renderFavoriteState(isChecked: Boolean) {
        stateFavoriteData.postValue(isChecked)
    }
}
