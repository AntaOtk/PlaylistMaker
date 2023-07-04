package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.player.data.Player
import com.example.playlistmaker.search.data.SearchHistory
import com.example.playlistmaker.search.data.TracksRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.player.domain.PlayerPresenter
import com.example.playlistmaker.search.domain.api.OneTrackRepository
import com.example.playlistmaker.search.domain.api.TrackHistoryRepository
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.player.domain.use_case.PlayControlImpl
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl


object Creator {
    private fun getTrackRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTrackRepository())
    }

    fun createPlayControl(playerPresenter: PlayerPresenter): PlayControlImpl {
        return PlayControlImpl(Player(), playerPresenter)
    }

    fun getOneTrackRepository(context: Context): OneTrackRepository {
        return SearchHistory(context)
    }

    fun getHistoryRepository(context: Context): TrackHistoryRepository {
        return SearchHistory(context)
    }

    fun getSharingInteractor(context: Context):SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator(),context)
    }

    fun getExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl()
    }

}