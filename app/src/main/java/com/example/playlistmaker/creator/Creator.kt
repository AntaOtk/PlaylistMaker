package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.player.data.Player
import com.example.playlistmaker.search.data.SearchHistoryRepository
import com.example.playlistmaker.search.data.TracksRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.player.domain.PlayerPresenter
import com.example.playlistmaker.search.domain.api.OneTrackRepository
import com.example.playlistmaker.search.domain.api.TrackHistoryRepository
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.player.domain.use_case.PlayControlImpl
import com.example.playlistmaker.search.data.LocalStorage


object Creator {
    private fun getTrackRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideTrackInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTrackRepository(context))
    }

    fun createPlayControl(playerPresenter: PlayerPresenter): PlayControlImpl {
        return PlayControlImpl(Player(), playerPresenter)
    }

    fun getOneTrackRepository(context: Context): OneTrackRepository {
        return SearchHistoryRepository(LocalStorage(context.getSharedPreferences("local_storage", Context.MODE_PRIVATE)))
    }

    fun getHistoryRepository(context: Context): TrackHistoryRepository {
        return SearchHistoryRepository(LocalStorage(context.getSharedPreferences("local_storage", Context.MODE_PRIVATE)))
    }

}