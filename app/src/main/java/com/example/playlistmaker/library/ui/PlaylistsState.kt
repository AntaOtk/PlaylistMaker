package com.example.playlistmaker.library.ui

import com.example.playlistmaker.library.domain.model.PlayList

sealed interface PlaylistsState {
    data class Content(
        val items: List<PlayList>
    ) : PlaylistsState

    object Empty : PlaylistsState
}