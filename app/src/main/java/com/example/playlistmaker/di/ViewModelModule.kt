package com.example.playlistmaker.di

import com.example.playlistmaker.library.ui.view_model.PlaylistLibraryViewModel
import com.example.playlistmaker.library.ui.view_model.TracksViewModel
import com.example.playlistmaker.main.ui.MainActivityViewModel
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.playlist.ui.PlaylistViewModel
import com.example.playlistmaker.playlist_creator.ui.PlayListCreatorViewModel
import com.example.playlistmaker.playlist_updeate.PlaylistUpdateViewModel
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        PlayerViewModel(get(), get(), get())
    }

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }
    viewModel {
        PlaylistLibraryViewModel(get())
    }
    viewModel {
        TracksViewModel(get())
    }

    viewModel { PlayListCreatorViewModel(get()) }
    viewModel { PlaylistUpdateViewModel(get()) }
    viewModel { MainActivityViewModel() }
    viewModel { PlaylistViewModel(get(), get()) }

}
