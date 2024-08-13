package com.example.playlistmaker.search.ui.fragments

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.SearchState
import com.example.playlistmaker.search.ui.view_model.SearchViewModel

@Composable
fun SearchScreen(viewModel: SearchViewModel) {
    val currentData by viewModel.stateData.observeAsState()
    Column {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.search_button),
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 22.sp
                )
            },
            backgroundColor = MaterialTheme.colors.background,
            modifier = Modifier.fillMaxWidth(),
            elevation = 0.dp
        )
        SearchTextField(viewModel)
        when (currentData) {
            is SearchState.Content -> SearchItem((currentData as SearchState.Content).tracks)
            is SearchState.Empty -> ErrorText((currentData as SearchState.Empty).message)
            is SearchState.Error -> ErrorText((currentData as SearchState.Error).errorMessage)
            is SearchState.Loading -> LoadingView()
            is SearchState.EmptyInput -> HistoryScreen((currentData as SearchState.EmptyInput).tracks)
            else -> HistoryScreen(mutableListOf())
        }
    }
}

@Composable
fun HistoryScreen(trackList: List<Track>) {
    if (trackList.isEmpty()) Text(text = stringResource(id = R.string.clear_history))
}

@Composable
fun LoadingView() {
    CircularProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.paddingClassic))
    )
}

@Composable
fun ErrorText(message: String) {
    Text(message)
}

@Composable
fun SearchTextField(viewModel: SearchViewModel) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.paddingClassic))
            .background(
                color = colorResource(R.color.search_color),
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.search_corner_radius))
            ),
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.small_search),
                contentDescription = null
            )
        },
        value = text,
        label = { Text(text = stringResource(id = R.string.search_button)) },
        keyboardActions = KeyboardActions { },
        onValueChange = { text = it },
        maxLines = 1,
    )
}