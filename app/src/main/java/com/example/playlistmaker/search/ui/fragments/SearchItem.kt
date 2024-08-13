package com.example.playlistmaker.search.ui.fragments

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.model.Track

@Composable
fun SearchItem(trackList: List<Track>) {
    for (track in trackList) {
        Row {
            Column() {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    style = MaterialTheme.typography.body1,
                    text = track.trackName
                )
                Text(text = track.artistName)
            }
            Icon(
                painter = painterResource(id = R.drawable.forward),
                contentDescription = null,
                tint = MaterialTheme.colors.onBackground
            )

        }
    }
}
