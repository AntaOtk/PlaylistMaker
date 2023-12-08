package com.example.playlistmaker.playlist_editor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.model.PlayList
import com.example.playlistmaker.playlist_creator.domain.PlayListCreatorInteractor
import com.example.playlistmaker.playlist_creator.ui.PlayListCreatorViewModel
import kotlinx.coroutines.launch
import java.io.File

class PlaylistEditorViewModel(val interactor: PlayListCreatorInteractor) : PlayListCreatorViewModel(
    interactor
) {

    private val saveLiveData = MutableLiveData<PlayList>()
    fun observeSaveState(): LiveData<PlayList> = saveLiveData

    fun savePlaylist(filePath: File, playList: PlayList) {
        viewModelScope.launch {
            if (fileDir != null) {
                saveImage(filePath, playList.id.toString())
            }
            playList.name = nameLiveData.value!!
            playList.description = descriptionLiveData.value ?: ""
            interactor.updatePlayList(playList).collect { playList ->
                renderState(playList)
            }
        }
    }

    fun renderState(playList: PlayList) {
        saveLiveData.postValue(playList)
    }
}