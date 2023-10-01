package com.example.playlistmaker.sharing.domain

import com.example.playlistmaker.library.domain.model.PlayList

interface SharingInteractor {
    fun shareApp()
    fun openTerms()
    fun openSupport()
    fun sharePlayList(playlist: PlayList)
}