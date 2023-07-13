package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track

interface OneTrackRepository {
    fun getTrack(): Track
}