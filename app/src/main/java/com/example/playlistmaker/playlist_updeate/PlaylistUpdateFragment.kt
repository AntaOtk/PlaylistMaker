package com.example.playlistmaker.playlist_updeate

import com.example.playlistmaker.playlist_creator.ui.PlaylistCreatorFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistUpdateFragment : PlaylistCreatorFragment() {
    private val viewModel by viewModel<PlaylistUpdateViewModel>()


}