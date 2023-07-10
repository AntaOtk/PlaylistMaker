package com.example.playlistmaker.player.domain.util

import java.text.SimpleDateFormat
import java.util.Locale

class TimeFormatter {

    companion object {
        private const val TIME_FORMAT = "mm:ss"
        const val ZERO_TIME = "00:00"

        fun format(time: Int): String {
            return SimpleDateFormat(
                TIME_FORMAT,
                Locale.getDefault()
            ).format(time)
        }
    }


}