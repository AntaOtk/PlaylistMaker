package com.example.playlistmaker.library.ui.fragments

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.model.PlayList
import com.example.playlistmaker.library.ui.PlaylistsState
import com.example.playlistmaker.library.ui.view_model.PlaylistLibraryViewModel
import com.example.playlistmaker.library.ui.view_model.TracksViewModel
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaLibraryScreen(
    tracksViewModel: TracksViewModel,
    playListsViewModel: PlaylistLibraryViewModel,
    onTrackClick: (Track) -> Unit,
    onPlaylistClick: (PlayList) -> Unit,
    onNewPlaylistClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val selectedTabIndex = remember { pagerState.currentPage }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        YPTopBar(title = stringResource(id = R.string.library_button))
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = pagerState.currentPage == 0,
                selectedContentColor = MaterialTheme.colorScheme.background,
                unselectedContentColor = MaterialTheme.colorScheme.background,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                text = { Text(text = stringResource(id = R.string.my_tracks)) }
            )

            Tab(
                selected = pagerState.currentPage == 1,
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onBackground,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
                text = { Text(text = stringResource(id = R.string.playlists)) },
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            when (page) {
                0 -> FavoriteTracksPage(
                    tracksViewModel = tracksViewModel,
                    onTrackClick = onTrackClick,
                )

                1 -> PlaylistsPage(
                    viewModel = playListsViewModel,
                    onPlaylistClick = onPlaylistClick,
                    onNewPlaylistClick = onNewPlaylistClick,
                )
            }
        }
    }
}


@Composable
fun PlaylistsPage(
    viewModel: PlaylistLibraryViewModel,
    onPlaylistClick: (PlayList) -> Unit,
    onNewPlaylistClick: () -> Unit,
) {
    val state by viewModel.stateLiveData.observeAsState(PlaylistsState.Empty)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        AddPlayListButton(
            onNewPlaylistClick = onNewPlaylistClick
        )
        when (state) {
            is PlaylistsState.Content -> {
                val playlists = (state as PlaylistsState.Content).items
                PlaylistsGrid(playlists = playlists, onPlaylistClick = onPlaylistClick)
            }

            PlaylistsState.Empty -> {
                EmptyPlayLists()
            }
        }
    }
}

@Composable
fun AddPlayListButton(
    onNewPlaylistClick: () -> Unit
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val textColor = MaterialTheme.colorScheme.secondary
    val fontFamily = FontFamily(Font(R.font.ys_display_medium))

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onNewPlaylistClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColor,
                contentColor = textColor
            ),
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .wrapContentWidth()
                .height(56.dp)
                .wrapContentHeight()
                .padding(top = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.new_playlist),
                fontFamily = fontFamily,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}