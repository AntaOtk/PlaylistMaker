package com.example.playlistmaker.sharing.domain.impl


import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.SharingRepository

class SharingInteractorImp(private val sharingRepository: SharingRepository) : SharingInteractor {
    override fun shareApp() {
        sharingRepository.shareApp()
    }

    override fun openTerms() {
        sharingRepository.openTerms()
    }

    override fun openSupport() {
        sharingRepository.openSupport()
    }

    override fun sharePlayList() {
        sharingRepository.sharePlayList()
    }
}
