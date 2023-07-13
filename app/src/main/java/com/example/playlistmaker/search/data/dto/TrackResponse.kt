package com.example.playlistmaker.search.data.dto


data class TrackResponse(
    val resultCount: Int,
    val expression: String,
    val results: List<TrackDto>
) : Response()