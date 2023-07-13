package com.example.playlistmaker.di

import com.example.playlistmaker.player.ui.viewmodel.PlayerViewModel
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        PlayerViewModel(get())
    }

    viewModel {
        SearchViewModel(get(),get(), androidContext())
    }

    viewModel {
        SettingsViewModel(get(),get())
    }
}
