package com.example.playlistmaker.sharing.domain.model


data class EmailData(
    val emailAddressee: Array<String>,
    val emailTopic: String,
    val emailMessage: String
)