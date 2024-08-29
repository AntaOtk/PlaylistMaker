package com.example.playlistmaker.search.ui.fragments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun TrackItemList(
    modifier: Modifier = Modifier,
    trackList: List<Track>,
    clickListener: (Track) -> Unit
) {
    Column(modifier) {
        for (track in trackList) {
            TrackCard(track, clickListener)
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TrackCard(track: Track, clickListener: (Track) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .clickable(onClick = { clickListener(track) })
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            GlideImage(
                modifier = Modifier
                    .clip(RoundedCornerShape(2.dp))
                    .size(45.dp),
                model = track.artworkUrl100,
                contentScale = ContentScale.Fit,
                loading = placeholder(R.drawable.placeholder),
                failure = placeholder(R.drawable.placeholder),
                contentDescription = track.trackName
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 16.dp)
                    .weight(5f),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    text = track.trackName,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Row() {
                    Text(
                        modifier = Modifier.weight(2f, fill = false),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        text = track.artistName,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.divider),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .size(8.dp)
                            .align(Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        text = SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(track.trackTimeMillis)
                    )

                }
            }
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .width(24.dp),
                painter = painterResource(id = R.drawable.forward),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )

        }
    }
}


@Preview()
@Composable()
fun ItemsPreview() {
    Column {
        TrackCard(track = Track(
            12,
            "3232",
            "123112311231123112311231123112311231123112311231",
            2222,
            "3232",
            "3232",
            "3232",
            "3232",
            "3232",
            "3232"
        ), { track -> 1 })
        TrackCard(track = Track(
            12,
            "3232",
            "1231121231",
            2222,
            "3232",
            "3232",
            "3232",
            "3232",
            "3232",
            "3232"
        ), { track -> 1 })
    }

}