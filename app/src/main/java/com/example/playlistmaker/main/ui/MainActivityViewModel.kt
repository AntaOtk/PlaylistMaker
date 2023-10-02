package com.example.playlistmaker.main.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.library.domain.model.PlayList
import com.example.playlistmaker.search.domain.model.Track

class MainActivityViewModel : ViewModel() {

    private val currentLiveData = MutableLiveData<Track>()
    fun setCurrentTrack(track: Track) {
        currentLiveData.postValue(track)
    }

    fun getCurrentTrack(): LiveData<Track> = currentLiveData

    private val playListLiveData = MutableLiveData<PlayList>()
    fun setPlayList(playList: PlayList) {
        playListLiveData.postValue(playList)
    }

    fun getPlayList(): LiveData<PlayList> = playListLiveData

}