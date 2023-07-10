package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.player.data.impl.PlayerClientImpl
import com.example.playlistmaker.player.domain.impl.PlayControlImpl
import com.example.playlistmaker.search.data.SearchHistoryRepository
import com.example.playlistmaker.search.data.TracksRepositoryImpl
import com.example.playlistmaker.search.data.local.LocalStorage
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.api.TrackHistoryRepository
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImp


object Creator {
    private fun getTrackRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideTrackInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTrackRepository(context))
    }

    fun getHistoryRepository(context: Context): TrackHistoryRepository {
        return SearchHistoryRepository(
            LocalStorage(
                context.getSharedPreferences(
                    "local_storage",
                    Context.MODE_PRIVATE
                )
            )
        )
    }

    fun createPlayControl(): PlayControlImpl {
        return PlayControlImpl(PlayerClientImpl())
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        val externalNavigator = ExternalNavigatorImpl(context)
        return SharingInteractorImp(externalNavigator, context)
    }

}