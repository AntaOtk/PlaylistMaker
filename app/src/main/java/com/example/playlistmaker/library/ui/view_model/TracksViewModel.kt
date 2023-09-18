package com.example.playlistmaker.library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.FavoriteTracksInteractor
import com.example.playlistmaker.library.ui.FavoriteState
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.launch

class TracksViewModel(private val interactor: FavoriteTracksInteractor) : ViewModel() {

    fun  fill() {
        viewModelScope.launch {
            interactor.getTracks()
                .collect { tracks ->
                    renderState(processResult(tracks))
                }
        }

    }

    private val stateFavoriteLiveData = MutableLiveData<FavoriteState>()
    fun observeState(): LiveData<FavoriteState> = stateFavoriteLiveData

    private fun processResult(tracks: List<Track>):FavoriteState {
        return if (tracks.isEmpty()) {
            FavoriteState.Empty
        } else {
            FavoriteState.Content(tracks)
        }
    }

    private fun renderState(state: FavoriteState) {
        stateFavoriteLiveData.postValue(state)
    }
}
