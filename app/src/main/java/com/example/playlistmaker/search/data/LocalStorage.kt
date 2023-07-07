package com.example.playlistmaker.search.data

import android.content.SharedPreferences
import com.example.playlistmaker.search.data.dto.TrackDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class LocalStorage(private val sharedPreferences: SharedPreferences) {

    private companion object {
        private const val HISTORY_KEY = "HISTORY_KEY"
        private const val MAXIMUM = 10
    }

    fun getTrack(): TrackDto {
        return read().get(0)
    }

    fun getTrackList(): List<TrackDto> {
        return read()
    }

    fun setTrack(track: TrackDto) {
        val tracks = read()
        if (!tracks.remove(track) && tracks.size >= MAXIMUM) tracks.removeAt(
            MAXIMUM - 1
        )
        tracks.add(0, track)
        write(tracks)
    }


    private fun read(): MutableList<TrackDto> {
        val json = sharedPreferences.getString(HISTORY_KEY, null)
            ?: return mutableListOf()
        val listOfMyClassObject: Type = object : TypeToken<ArrayList<TrackDto>?>() {}.type
        return Gson().fromJson(json, listOfMyClassObject)
    }


    private fun write(tracks: MutableList<TrackDto>) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit()
            .putString(HISTORY_KEY, json)
            .apply()
    }

    fun clear() {
        sharedPreferences.edit()
            .remove(HISTORY_KEY)
            .apply()
    }


}