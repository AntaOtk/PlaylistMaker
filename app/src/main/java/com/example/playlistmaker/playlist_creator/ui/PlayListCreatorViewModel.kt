package com.example.playlistmaker.playlist_creator.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.playlist_creator.domain.PlayListCreatorInteractor
import java.net.URI

class PlayListCreatorViewModel(private val interactor: PlayListCreatorInteractor) : ViewModel() {

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
        if (playListName == changedText) {
            return
        }
        this.playListName = changedText
    }

    fun setDescription(changedText: String) {
        if (description == changedText) {
            return
        }
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

    fun getMessage(): String {
        return "Плейлист $playListName создан"
    }

    fun checkInput(): Boolean {
        return (fileDir != null) || playListName.isNotEmpty() || description.isNotEmpty()
    }
}