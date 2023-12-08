package com.example.playlistmaker.library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.PlaylistLibraryInteractor
import com.example.playlistmaker.library.domain.model.PlayList
import com.example.playlistmaker.library.ui.PlaylistsState
import kotlinx.coroutines.launch

class PlaylistLibraryViewModel(val interactor: PlaylistLibraryInteractor) : ViewModel() {


    private val stateLiveData = MutableLiveData<PlaylistsState>()
    fun observeState(): LiveData<PlaylistsState> = stateLiveData

    fun fill() {
        viewModelScope.launch {
            interactor.getPlayLists()
                .collect { playLists ->
                    renderState(processResult(playLists))
                }
        }
    }

    private fun processResult(alboms: List<PlayList>): PlaylistsState {
        return if (alboms.isEmpty()) {
            PlaylistsState.Empty
        } else {
            PlaylistsState.Content(alboms)
        }
    }

    private fun renderState(state: PlaylistsState) {
        stateLiveData.postValue(state)
    }
}

