package com.example.playlistmaker.search.ui.fragments

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R

@Composable
fun EmptyMessage(
    modifier: Modifier = Modifier.fillMaxSize(),
    messageText: String
) {
    Column {
        Image(
            modifier = modifier.align(Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.search_message),
            contentDescription = null,
        )
        Text(
            modifier = modifier.align(Alignment.CenterHorizontally),
            text = messageText,
            style = MaterialTheme.typography.displayMedium.copy(
                MaterialTheme.colorScheme.secondary
            )
        )
    }
}

@Composable
fun ErrorConnectionMessage(
    modifier: Modifier = Modifier.fillMaxSize(),
    messageText: String,
    onButtonClick: () -> Unit
) {
    Column {
        Image(
            modifier = modifier.align(Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.internet_message),
            contentDescription = null,
        )
        Text(
            modifier = modifier.align(Alignment.CenterHorizontally),
            text = messageText,
            style = MaterialTheme.typography.displayMedium.copy(
                MaterialTheme.colorScheme.secondary
            )
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
                text = stringResource(id = R.string.repeat),
                style = MaterialTheme.typography.displayMedium.copy(
                    color = MaterialTheme.colorScheme.surfaceTint
                )
            )
        }
    }
}