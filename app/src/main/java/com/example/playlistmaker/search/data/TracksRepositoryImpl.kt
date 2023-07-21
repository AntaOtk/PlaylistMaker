package com.example.playlistmaker.search.data

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.dto.SearchRequest
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.data.mapper.TrackMapper
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.util.Resource

class TracksRepositoryImpl(private val networkClient: NetworkClient, private val mapper: TrackMapper, private val context: Context) :
    TracksRepository {

    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(SearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error(context.getString(R.string.no_interrnet_conection))
            }

            200 -> {
                Resource.Success((response as TrackResponse).results.map {
                    mapper.trackMap(it)
                })
            }

            else -> {
                Resource.Error(context.getString(R.string.no_interrnet_conection))
            }
        }
    }
    override fun getMessage(): String {
        return context.getString(R.string.nothing_found)
    }
}
