package com.example.playlistmaker.di

import com.example.playlistmaker.player.domain.PlayControl
import com.example.playlistmaker.player.domain.impl.PlayControlImpl
import com.example.playlistmaker.player.domain.util.PlayerState
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImp
import org.koin.dsl.module

val interactorModule = module {

    factory { PlayerState.PREPARED }

    single<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImp(get())
    }

    factory<PlayControl> {
        PlayControlImpl(get(),get())
    }
}
