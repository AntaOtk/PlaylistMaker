package com.example.playlistmaker.library.domain.model

data class PlayList(
    var id: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
    var trackCount: Long,
    var tracks: String?
)