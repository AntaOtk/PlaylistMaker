package com.example.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.FavoriteTracksInteractor
import com.example.playlistmaker.library.domain.PlaylistLibraryInteractor
import com.example.playlistmaker.library.domain.model.PlayList
import com.example.playlistmaker.player.domain.util.PlayerState
import com.example.playlistmaker.player.services.AudioPlayerControl
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val favoriteInteractor: FavoriteTracksInteractor,
    private val playlistInteractor: PlaylistLibraryInteractor
) :
    ViewModel() {


    private val stateLiveData = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observeState(): LiveData<PlayerState> = stateLiveData

    private val stateFavoriteData = MutableLiveData<Boolean>()
    fun observeFavoriteState(): LiveData<Boolean> = stateFavoriteData

    private val playListsLiveData = MutableLiveData<List<PlayList>>()
    fun observePlaylistState(): LiveData<List<PlayList>> = playListsLiveData

    private val addLiveData = MutableLiveData<Pair<String, Boolean>>()
    fun observeAddDtate(): LiveData<Pair<String, Boolean>> = addLiveData

    private var audioPlayerControl: AudioPlayerControl? = null

    fun setAudioPlayerControl(audioPlayerControl: AudioPlayerControl) {
        this.audioPlayerControl = audioPlayerControl

        viewModelScope.launch {
            audioPlayerControl.getPlayerState().collect {
                stateLiveData.postValue(it)
            }
        }
    }


    fun showNotification(){
        audioPlayerControl?.showNotification()
    }

    fun hideNotification(){
        audioPlayerControl?.hideNotification()
    }

    fun onPlayerButtonClicked() {
        if (stateLiveData.value is PlayerState.Playing) {
            audioPlayerControl?.pausePlayer()
        } else {
            audioPlayerControl?.startPlayer()
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerControl = null
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

    private fun renderToastState(result: Pair<String, Boolean>) {
        addLiveData.postValue(result)
    }


    fun addToPlaylist(track: Track, playList: PlayList) {
        viewModelScope.launch {
            renderToastState(Pair(playList.name, playlistInteractor.addTrack(track, playList)))
        }
    }

    fun renderPlayLists() {
        viewModelScope.launch {
            playlistInteractor.getPlayLists()
                .collect { playLists ->
                    playListsLiveData.postValue(playLists)
                }
        }
    }

    fun removeAudioPlayerControl() {
        audioPlayerControl = null
    }


}
