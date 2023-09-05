package com.example.playlistmaker.search.data

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.search.data.dto.SearchRequest
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.data.mapper.TrackConvertor
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase,
    private val mapper: TrackConvertor,
    private val context: Context
) :
    TracksRepository {

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(SearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(context.getString(R.string.no_interrnet_conection)))
            }

            200 -> {
                with(response as TrackResponse) {
                    val favoriteTracks = appDatabase.trackDao().getTrackId()
                    val data = results.map {
                        mapper.trackMap(it,favoriteTracks.contains(it.trackId))
                    }
                    emit(Resource.Success(data))
                }
            }

            else -> {
                emit(Resource.Error(context.getString(R.string.no_interrnet_conection)))
            }
        }
    }

    override fun getMessage(): String {
        return context.getString(R.string.nothing_found)
    }
}
