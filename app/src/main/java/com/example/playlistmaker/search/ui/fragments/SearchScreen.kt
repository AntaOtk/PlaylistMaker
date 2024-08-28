package com.example.playlistmaker.search.ui.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R
import com.example.playlistmaker.library.ui.fragments.YPTopBar
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.ui.SearchState
import com.example.playlistmaker.search.ui.view_model.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onTrackClickDebounce: (Track) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentData by viewModel.stateData.observeAsState()
    Column {
        YPTopBar(stringResource(id = R.string.search_button))
        SearchTextField(viewModel)
        when (currentData) {
            is SearchState.Content -> TrackItemList(
                (currentData as SearchState.Content).tracks,
                onTrackClickDebounce
            )

            is SearchState.Empty -> EmptyMessage(
                modifier,
                (currentData as SearchState.Empty).message
            )

            is SearchState.Error -> ErrorConnectionMessage(
                modifier, (currentData as SearchState.Error).errorMessage
            ) { viewModel.searchDebounce() }

            is SearchState.Loading -> LoadingView()
            is SearchState.EmptyInput -> HistoryScreen(
                (currentData as SearchState.EmptyInput).tracks,
                onTrackClickDebounce
            )

            else -> BasePage()
        }
    }
}

@Composable
fun BasePage() {
    Column() {
        Text(text = stringResource(id = R.string.clear_history))
    }
}

@Composable
fun HistoryScreen(trackList: List<Track>, clickListener: (Track) -> Unit) {
    Column {
        TrackItemList(
            trackList = trackList,
            clickListener
        )

    }
}

@Composable
fun LoadingView() {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }

}

@Composable
fun SearchTextField(viewModel: SearchViewModel) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(colorResource(id = R.color.hell_gray_color))
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp),
            value = text,
            onValueChange = {
                viewModel.onTextChanged(it.text)
                viewModel.searchDebounce()
                text = it
            },
            textStyle = TextStyle.Default.copy(fontSize = 28.sp, color = Color.Black),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = null,
                    tint = colorResource(id = R.color.gray_color)
                )
            },
            trailingIcon = {
                if (text.text.isNotEmpty()) {
                    IconButton(onClick = { viewModel.clear() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.cross),
                            contentDescription = null,
                            tint = colorResource(id = R.color.gray_color)
                        )
                    }
                }
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.search_button),
                    color = colorResource(id = R.color.hell_gray_color)
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                focusedTextColor = MaterialTheme.colorScheme.onTertiary,
                unfocusedTextColor = MaterialTheme.colorScheme.onTertiary,

            ),
            singleLine = true,
        )
    }
}