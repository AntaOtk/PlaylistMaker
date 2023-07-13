package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.util.Resource

interface TracksRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}