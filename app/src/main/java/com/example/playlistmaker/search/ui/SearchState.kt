package com.example.playlistmaker.search.ui

import com.example.playlistmaker.search.domain.model.Track

sealed interface SearchState {

    data object Loading : SearchState
    data object Default : SearchState

    data class Content(
        val tracks: List<Track>
    ) : SearchState

    data class Error(
        val errorMessage: String
    ) : SearchState

    data class EmptyInput(
        val tracks: List<Track>
    ) : SearchState


    data class Empty(
        val message: String
    ) : SearchState

}