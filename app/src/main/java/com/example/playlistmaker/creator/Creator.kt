package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.data.Player
import com.example.playlistmaker.data.SearchHistory
import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.PlayerPresenter
import com.example.playlistmaker.domain.api.OneTrackRepository
import com.example.playlistmaker.domain.api.TrackHistoryRepository
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.use_case.PlayControlImpl


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

}