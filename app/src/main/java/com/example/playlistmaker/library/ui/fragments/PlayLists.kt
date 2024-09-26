package com.example.playlistmaker.library.ui.fragments

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.model.PlayList
import com.example.playlistmaker.library.ui.PlaylistsState
import com.example.playlistmaker.library.ui.view_model.PlaylistLibraryViewModel
import com.example.playlistmaker.search.ui.fragments.EmptyMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun PlaylistsPage(
    viewModel: PlaylistLibraryViewModel,
    onPlaylistClick: (PlayList) -> Unit,
    onNewPlaylistClick: () -> Unit,
) {
    val state by viewModel.stateLiveData.observeAsState()
    viewModel.fill()
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

            else -> {
                EmptyMessage(messageText = stringResource(id = R.string.playlist_message))
            }
        }
    }
}

@Composable
fun AddPlayListButton(
    onNewPlaylistClick: () -> Unit
) {
    val backgroundColor = MaterialTheme.colorScheme.secondary
    val textColor = MaterialTheme.colorScheme.background
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

@Composable
fun PlaylistsGrid(
    playlists: List<PlayList>,
    onPlaylistClick: (PlayList) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(playlists.size) { index ->
            PlaylistItem(
                playlist = playlists[index],
                onClick = { onPlaylistClick(playlists[index]) }
            )
        }
    }
}

@Composable
fun PlaylistItem(
    playlist: PlayList,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    val placeholder = painterResource(id = R.drawable.placeholder)

    DisposableEffect(playlist.imageUrl) {
        val job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val futureBitmap = playlist.imageUrl.let { uri ->
                    Glide.with(context)
                        .asBitmap()
                        .load(uri)
                        .submit()
                        .get()
                }
                withContext(Dispatchers.Main) {
                    bitmap = futureBitmap
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    bitmap = null
                }
            }
        }
        onDispose { job.cancel() }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(4.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxWidth()
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                )
            } else {
                Image(
                    painter = placeholder,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Text(
            text = playlist.name,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 12.sp,
            maxLines = 1,
            modifier = Modifier
                .padding(top = 4.dp)
        )

        Text(
            text = LocalContext.current.resources.getQuantityString(
                R.plurals.tracksContOfList, playlist.tracks.size, playlist.tracks.size
            ),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 12.sp,
            maxLines = 1
        )
    }
}