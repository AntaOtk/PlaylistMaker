package com.example.playlistmaker.searchlist

import com.google.gson.annotations.SerializedName

class Track(
    @SerializedName("trackName")
    val trackName: String,
    @SerializedName("artistName")
    val artistName: String,
    @SerializedName("trackTimeMillis")
    val trackTimeMillis: Long,
    @SerializedName("artworkUrl100")
    val artworkUrl100: String
)