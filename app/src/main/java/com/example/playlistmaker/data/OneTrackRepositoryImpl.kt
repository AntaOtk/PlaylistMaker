package com.example.playlistmaker.data

import android.content.Context
import com.example.playlistmaker.domain.api.OneTrackRepository
import com.example.playlistmaker.domain.models.Track

class OneTrackRepositoryImpl(context: Context): OneTrackRepository {
    private val searchHistory = SearchHistory(context)

    override fun getTrack(): Track {
        return searchHistory.read().get(0)
    }
}