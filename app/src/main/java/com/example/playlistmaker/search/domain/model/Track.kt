package com.example.playlistmaker.search.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Track(
    @SerializedName("trackName")
    val trackName: String,
    @SerializedName("artistName")
    val artistName: String,
    @SerializedName("trackTimeMillis")
    val trackTimeMillis: Long,
    @SerializedName("artworkUrl100")
    val artworkUrl100: String,
    @SerializedName("collectionName")
    val collectionName: String,
    @SerializedName("releaseDate")
    val releaseDate: String,
    @SerializedName("primaryGenreName")
    val primaryGenreName: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("previewUrl")
    val previewUrl: String
) : Serializable