package com.example.playlistmaker.di

import com.example.playlistmaker.libraby.ui.viewmodel.PlaylistViewModel
import com.example.playlistmaker.libraby.ui.viewmodel.TracksViewModel
import com.example.playlistmaker.player.ui.viewmodel.PlayerViewModel
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        PlayerViewModel(get())
    }

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }
    viewModel {
        PlaylistViewModel()
    }
    viewModel {
        TracksViewModel()
    }
}
