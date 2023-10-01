package com.example.playlistmaker.playlist_creator.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.playlist_creator.domain.PlayListCreatorInteractor
import kotlinx.coroutines.launch
import java.io.File
import java.net.URI

open class PlayListCreatorViewModel(private val interactor: PlayListCreatorInteractor) : ViewModel() {

    private var playListName: String = ""
    private var description: String = ""
    private var fileDir: Uri? = null

    suspend fun savePlaylist(toURI: URI): String {
        val fileName = if (fileDir != null) {
            (interactor.savePlaylist(playListName, description, toURI.toString()))
        } else {
            interactor.savePlaylist(playListName, description, "")
        }
        return fileName.toString()
    }


    fun setName(changedText: String) {
        this.playListName = changedText
    }

    fun setDescription(changedText: String) {
        this.description = changedText
    }

    fun setUri(uri: Uri) {
        if (fileDir == uri) {
            return
        }
        this.fileDir = uri
    }

    fun getUri(): Uri? {
        return fileDir
    }

    fun getName(): String {
        return playListName
    }

    fun checkInput(): Boolean {
        return (fileDir != null) || playListName.isNotEmpty() || description.isNotEmpty()
    }

    fun saveImage(filePath: File, savePlaylist: String, uri: Uri) {
        viewModelScope.launch {
            interactor.saveImage(filePath,savePlaylist, uri)
        }
    }
}