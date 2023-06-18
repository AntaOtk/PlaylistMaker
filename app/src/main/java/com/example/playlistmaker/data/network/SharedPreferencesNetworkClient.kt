package com.example.playlistmaker.data.network

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.presentation.PRACTICUM_PREFERENCES
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class SharedPreferencesNetworkClient(context: Context){

    companion object {
        private const val TRACKS_KEY = "track_key"
        private const val MAXIMUM = 10
    }


    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PRACTICUM_PREFERENCES,
        Context.MODE_PRIVATE
    )

    override fun doRequest(dto: Any): Response {
        if (dto is HistoryRequest) {
            val json = sharedPreferences.getString(TRACKS_KEY, null) ?: return mutableListOf()
            val listOfMyClassObject: Type = object : TypeToken<ArrayList<TrackDto>?>() {}.type
            val body = Gson().fromJson(json, listOfMyClassObject)
            return body.apply { }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}