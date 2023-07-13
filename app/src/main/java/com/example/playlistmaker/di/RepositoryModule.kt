package com.example.playlistmaker.di

import com.example.playlistmaker.player.data.impl.PlayerClientImpl
import com.example.playlistmaker.player.domain.PlayerClient
import com.example.playlistmaker.search.data.TrackHistoryRepositoryImpl
import com.example.playlistmaker.search.data.TracksRepositoryImpl
import com.example.playlistmaker.search.domain.api.TrackHistoryRepository
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.sharing.data.SharingRepositoryImpl
import com.example.playlistmaker.sharing.domain.SharingRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single<TrackHistoryRepository> {
        TrackHistoryRepositoryImpl(get())
    }

    single <SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<SharingRepository> {
        SharingRepositoryImpl(get(), androidContext())
    }

    factory<PlayerClient> {
        PlayerClientImpl(get())
    }
}
