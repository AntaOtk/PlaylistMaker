package com.example.playlistmaker.playlist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.model.PlayList
import com.example.playlistmaker.playlist.domain.PlayListInteractor
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.sharing.domain.SharingInteractor
import kotlinx.coroutines.launch

class PlaylistViewModel(
    val interactor: PlayListInteractor,
    val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlayList>()
    fun observeState(): LiveData<PlayList> = stateLiveData

    private val tracksLiveData = MutableLiveData<List<Track>>()
    fun observeTracks(): LiveData<List<Track>> = tracksLiveData

    fun getCurrentPlayList(playList: PlayList) {
        renderState(playList)
    }

    fun getUpdatePlayList() {
        viewModelScope.launch {
            stateLiveData.value?.let {
                interactor.getPlayList(it.id)
                    .collect { playList ->
                        renderState(playList)
                    }
            }
        }
    }

    fun getTracks() {
        viewModelScope.launch {
            interactor.getTracks(stateLiveData.value!!)
                .collect { tracks ->
                    renderTracks(tracks)
                }
        }
    }

    fun getTracksTime(tracks: List<Track>): Int {
        return interactor.getPlayListTime(tracks)
    }

    fun sharePlayList(playlist: PlayList) {
        sharingInteractor.sharePlayList(playlist)

    }

    fun deletePlayList(playlist: PlayList) {
        viewModelScope.launch {
            interactor.delete(playlist)
        }
    }

    fun removeTrack(track: Track, playList: PlayList) {
        viewModelScope.launch {
            interactor.removeTrack(track, playList)
            interactor.getPlayList(playList.id).collect {
                renderState(it)
            }
        }
    }

    private fun renderState(state: PlayList) {
        stateLiveData.postValue(state)
    }

    private fun renderTracks(tracks: List<Track>) {
        tracksLiveData.postValue(tracks)
    }
}
