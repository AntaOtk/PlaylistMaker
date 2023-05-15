package com.example.playlistmaker

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlistmaker.searchlist.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


const val TRACKS_KEY = "track_key"
const val MAXIMUM = 10

class SearchHistory(context: Context) {


    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PRACTICUM_PREFERENCES,
        MODE_PRIVATE
    )


    fun setTrack(track: Track) {
        val tracks = read()
        if (!tracks.remove(track) && tracks.size >= MAXIMUM) tracks.removeAt(MAXIMUM - 1)
        tracks.add(0, track)
        write(tracks)
    }


    fun read(): MutableList<
            Track> {
        val json = sharedPreferences.getString(TRACKS_KEY, null) ?: return mutableListOf()
        val listOfMyClassObject: Type = object : TypeToken<ArrayList<Track>?>() {}.type
        return Gson().fromJson(json, listOfMyClassObject)
    }


    fun write(tracks: MutableList<Track>) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit()
            .putString(TRACKS_KEY, json)
            .apply()
    }

    fun clear(sharedPreferences: SharedPreferences){
        sharedPreferences.edit()
            .remove(TRACKS_KEY)
            .apply()
    }

}