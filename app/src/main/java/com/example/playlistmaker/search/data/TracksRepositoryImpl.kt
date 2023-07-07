package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.SearchRequest
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.util.Resource

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    private val mapper = TrackMapper()

    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response =  networkClient.doRequest(SearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }
            200 -> {
                Resource.Success((response as TrackResponse).results.map {
                    mapper.trackMap(it)})
            }
            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}
