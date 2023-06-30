package com.example.playlistmaker.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlistmaker.PRACTICUM_PREFERENCES
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.api.OneTrackRepository
import com.example.playlistmaker.domain.api.TrackHistoryRepository
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class SearchHistory(context: Context) : OneTrackRepository, TrackHistoryRepository {

    companion object {
        private const val TRACKS_KEY = "track_key"
        private const val MAXIMUM = 10
    }


    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PRACTICUM_PREFERENCES,
        MODE_PRIVATE
    )

    override fun getTrack(): Track {
        val track = read().get(0)
        return trackMap(track)
    }

    override fun getTrackList(): List<Track> {
        return read().map {
            trackMap(it)
        }
    }

    override fun setTrack(track: Track) {
        val tracks = read()
        val trackDto = TrackDto(
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
        if (!tracks.remove(trackDto) && tracks.size >= MAXIMUM) tracks.removeAt(MAXIMUM - 1)
        tracks.add(0, trackDto)
        write(tracks)
    }


    private fun read(): MutableList<TrackDto> {
        val json = sharedPreferences.getString(TRACKS_KEY, null) ?: return mutableListOf()
        val listOfMyClassObject: Type = object : TypeToken<ArrayList<TrackDto>?>() {}.type
        return Gson().fromJson(json, listOfMyClassObject)
    }


    private fun write(tracks: MutableList<TrackDto>) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit()
            .putString(TRACKS_KEY, json)
            .apply()
    }

    override fun clear() {
        sharedPreferences.edit()
            .remove(TRACKS_KEY)
            .apply()
    }

    private fun trackMap(track: TrackDto): Track {
        return Track(
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

}