package com.example.playlistmaker.library.ui.fragments

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.playlistmaker.R
import com.example.playlistmaker.library.ui.FavoriteState
import com.example.playlistmaker.library.ui.view_model.TracksViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.fragments.TrackItemList

@Composable
fun FavoriteTracksPage(
    tracksViewModel: TracksViewModel,
    onTrackClick: (Track) -> Unit,
) {
    val state by tracksViewModel.stateFavoriteLiveData.observeAsState(FavoriteState.Empty)

    when (state) {
        is FavoriteState.Content -> {
            val tracks = (state as FavoriteState.Content).tracks
            TrackItemList(
                tracks,
                onTrackClick
            )
        }

        is FavoriteState.Empty -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = stringResource(id = R.string.tracks_message))
            }
        }
    }
}
