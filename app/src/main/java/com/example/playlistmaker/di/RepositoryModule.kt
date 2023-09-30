package com.example.playlistmaker.di

import com.example.playlistmaker.library.data.FavoriteTracksRepositoryImpl
import com.example.playlistmaker.library.data.JsonMapper
import com.example.playlistmaker.library.data.PlayListDbMapper
import com.example.playlistmaker.library.data.PlaylistRepositoryImpl
import com.example.playlistmaker.library.data.TrackDbMapper
import com.example.playlistmaker.library.domain.FavoriteTracksRepository
import com.example.playlistmaker.library.domain.PlaylistRepository
import com.example.playlistmaker.player.data.impl.PlayerClientImpl
import com.example.playlistmaker.player.domain.PlayerClient
import com.example.playlistmaker.playlist_creator.data.local.FileRepositoryImpl
import com.example.playlistmaker.playlist_creator.domain.FileRepository
import com.example.playlistmaker.search.data.TrackHistoryRepositoryImpl
import com.example.playlistmaker.search.data.TracksRepositoryImpl
import com.example.playlistmaker.search.data.mapper.TrackMapper
import com.example.playlistmaker.search.domain.api.TrackHistoryRepository
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.sharing.data.SharingRepositoryImpl
import com.example.playlistmaker.sharing.domain.SharingRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    single { TrackMapper() }
    single { TrackDbMapper() }
    single { PlayListDbMapper() }
    single { JsonMapper(get()) }



    single<TracksRepository> {
        TracksRepositoryImpl(get(),get(),androidContext())
    }

    single<TrackHistoryRepository> {
        TrackHistoryRepositoryImpl(get(),get())
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

    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(get(), get())
    }

    single<PlaylistRepository> { PlaylistRepositoryImpl(get(),get(),get()) }

    single<FileRepository> { FileRepositoryImpl(get()) }
}
