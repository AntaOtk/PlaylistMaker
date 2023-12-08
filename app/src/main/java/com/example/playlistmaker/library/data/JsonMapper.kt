package com.example.playlistmaker.library.data

import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class JsonMapper(
    private val gson: Gson
) {
    fun convertToList(jsonList: String?): MutableList<Track> {
        val listOfMyClassObject: Type = object : TypeToken<ArrayList<Track>?>() {}.type
        return if (!jsonList.isNullOrEmpty()) gson.fromJson(jsonList, listOfMyClassObject) else mutableListOf()
    }


    fun convertFromList(tracks: MutableList<Track>): String {
        return gson.toJson(tracks)
    }
}
