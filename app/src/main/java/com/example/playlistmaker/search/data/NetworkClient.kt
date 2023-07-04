package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}