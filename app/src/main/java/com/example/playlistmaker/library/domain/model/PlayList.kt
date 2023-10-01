package com.example.playlistmaker.library.domain.model

import com.example.playlistmaker.search.domain.model.Track

data class PlayList(
    var id: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
    var trackCount: Long,
    var tracks: MutableList<Track>
)