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
import com.example.playlistmaker.search.ui.fragments.EmptyMessage
import com.example.playlistmaker.search.ui.fragments.TrackItemList

@Composable
fun FavoriteTracksPage(
    modifier: Modifier = Modifier.fillMaxSize(),
    tracksViewModel: TracksViewModel,
    onTrackClick: (Track) -> Unit,
) {
    val state by tracksViewModel.stateFavoriteLiveData.observeAsState(FavoriteState.Empty)
    tracksViewModel.fill()
    when (state) {
        is FavoriteState.Content -> {
            val tracks = (state as FavoriteState.Content).tracks
            TrackItemList(
                modifier,
                tracks,
                onTrackClick
            )
        }

        is FavoriteState.Empty -> {
            EmptyMessage(messageText = stringResource(id = R.string.tracks_message))
        }
    }
}
