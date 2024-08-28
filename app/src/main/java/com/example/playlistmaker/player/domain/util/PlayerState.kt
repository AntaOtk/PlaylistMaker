package com.example.playlistmaker.player.domain.util

sealed class PlayerState(val buttonState: Boolean, val progress: String) {

    class Default : PlayerState(false, "00:00")

    class Prepared : PlayerState(false, "00:00")

    class Playing(progress: String) : PlayerState(true, progress)

    class Paused(progress: String) : PlayerState(false, progress)

    class Finished : PlayerState(false, "00:00")
}