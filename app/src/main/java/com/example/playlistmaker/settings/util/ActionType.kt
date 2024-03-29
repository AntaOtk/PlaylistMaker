package com.example.playlistmaker.settings.util

sealed interface ActionType {
    data class Theme( val settings: Boolean) : ActionType
    object Share : ActionType
    object Support : ActionType
    object Term : ActionType
}




