package com.example.playlistmaker.search.ui.fragments

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.library.ui.fragments.YPTopBar
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.SearchState
import com.example.playlistmaker.search.ui.view_model.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onTrackClickDebounce: (Track) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentData by viewModel.stateData.observeAsState(SearchState.Default)
    var text by remember { mutableStateOf("") }
    Column {
        YPTopBar(stringResource(id = R.string.search_button))
        SearchTextField(text = text,
            hint = stringResource(R.string.search_button),
            onTextChange = { inputText ->
                viewModel.onTextChanged(inputText)
                viewModel.searchDebounce()
                text = inputText
            })
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(8.dp))
        when (currentData) {
            is SearchState.Content -> TrackItemList(
                modifier = modifier.fillMaxSize(),
                trackList = (currentData as SearchState.Content).tracks,
                clickListener = onTrackClickDebounce
            )

            is SearchState.Empty -> EmptyMessage(
                messageText = (currentData as SearchState.Empty).message
            )

            is SearchState.Error -> ErrorConnectionMessage(messageText = (currentData as SearchState.Error).errorMessage,
                onButtonClick = {
                    viewModel.repeatSearch()
                })

            is SearchState.Loading -> LoadingView()

            is SearchState.EmptyInput -> HistoryScreen(modifier = modifier,
                trackList = (currentData as SearchState.EmptyInput).tracks,
                clickListener = onTrackClickDebounce,
                onButtonClick = { viewModel.clear() })

            else -> Unit
        }
    }
}

@Composable
fun HistoryScreen(
    modifier: Modifier,
    trackList: List<Track>,
    clickListener: (Track) -> Unit,
    onButtonClick: () -> Unit
) {
    if (trackList.isNotEmpty()) {
        Column() {
            Text(
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
                color = MaterialTheme.colorScheme.secondary,
                text = stringResource(id = R.string.clear_history)
            )
            TrackItemList(
                trackList = trackList, clickListener = clickListener
            )
            Button(
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 24.dp),
                onClick = onButtonClick,
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    contentColor = MaterialTheme.colorScheme.background,
                    disabledContentColor = Color.Unspecified,
                    disabledContainerColor = Color.Unspecified
                )
            ) {
                Text(
                    text = stringResource(id = R.string.clear_history),
                    style = MaterialTheme.typography.displayMedium.copy(
                        color = MaterialTheme.colorScheme.surfaceTint
                    )
                )
            }
        }
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
