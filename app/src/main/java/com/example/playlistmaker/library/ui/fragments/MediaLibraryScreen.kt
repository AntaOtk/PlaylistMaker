package com.example.playlistmaker.library.ui.fragments

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.model.PlayList
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
    val pagerState = rememberPagerState(pageCount = { 2 }, initialPage = 0)
    val selectedTabIndex = remember { mutableIntStateOf(pagerState.currentPage) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        YPTopBar(title = stringResource(id = R.string.library_button))
        TabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = selectedTabIndex.intValue,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.secondary,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    color = MaterialTheme.colorScheme.onTertiary,
                    height = 2.dp
                )
            },
            divider = {
                Divider(
                    color = Color.Transparent,
                    thickness = 0.dp
                )
            }
        ) {
            Tab(
                selected = selectedTabIndex.intValue == 0,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(0)
                        Log.d("my panic", pagerState.currentPage.toString())
                    }
                },
                text = { Text(text = stringResource(id = R.string.my_tracks)) }
            )

            Tab(
                selected = selectedTabIndex.intValue == 1,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(1)
                        Log.d("my panic", pagerState.currentPage.toString())
                    }
                },
                text = { Text(text = stringResource(id = R.string.playlists)) },
            )
        }
        Spacer(modifier = Modifier.fillMaxWidth().height(8.dp))
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
