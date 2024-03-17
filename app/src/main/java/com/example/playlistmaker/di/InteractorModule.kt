package com.example.playlistmaker.di

import com.example.playlistmaker.library.domain.FavoriteTracksInteractor
import com.example.playlistmaker.library.domain.PlaylistLibraryInteractor
import com.example.playlistmaker.library.domain.db.FavoriteTracksInteractorImpl
import com.example.playlistmaker.library.domain.impl.PlaylistLibraryInteractorImpl
import com.example.playlistmaker.player.domain.PlayControl
import com.example.playlistmaker.player.domain.impl.PlayControlImpl
import com.example.playlistmaker.player.domain.util.PlayerState
import com.example.playlistmaker.playlist.domain.PlayListInteractor
import com.example.playlistmaker.playlist.domain.PlayListInteractorImpl
import com.example.playlistmaker.playlist_creator.domain.PlayListCreatorInteractor
import com.example.playlistmaker.playlist_creator.domain.PlayListCreatorInteractorImpl
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImp
import org.koin.dsl.module

val interactorModule = module {

    factory { PlayerState.INIT }

    single<TracksInteractor> {
        TracksInteractorImpl(get(), get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImp(get())
    }

    single<PlaylistLibraryInteractor> {
        PlaylistLibraryInteractorImpl(get())
    }

    factory<PlayControl> {
        PlayControlImpl(get(), get())
    }

    single<FavoriteTracksInteractor> {
        FavoriteTracksInteractorImpl(get())
    }

    single<PlayListCreatorInteractor> {
        PlayListCreatorInteractorImpl(get(), get())
    }

    single<PlayListInteractor> { PlayListInteractorImpl(get()) }
}
