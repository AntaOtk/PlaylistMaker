package com.example.playlistmaker.library.ui

import com.example.playlistmaker.search.domain.model.Track

sealed interface FavoriteState {
    data class Content(
        val tracks: List<Track>
    ) : FavoriteState

    object Empty : FavoriteState
}
