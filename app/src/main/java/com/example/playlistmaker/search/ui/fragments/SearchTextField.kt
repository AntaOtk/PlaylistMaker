package com.example.playlistmaker.search.ui.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.theme.BlueText
import com.example.playlistmaker.theme.YaBlack

@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    text: String = "",
    hint: String = "",
    onTextChange: (String) -> Unit
) {
    Box {
        BasicTextField(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            value = text,
            onValueChange = onTextChange,
            singleLine = true,
            cursorBrush = SolidColor(BlueText),
            textStyle = MaterialTheme.typography.titleSmall.copy(
                color = YaBlack
            ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = null,
                        tint = colorResource(id = R.color.gray_color)
                    )
                    Box(
                        modifier = modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                modifier = Modifier.padding(start = 8.dp),
                                text = hint,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                            )
                        }
                        innerTextField()
                    }
                    if (text.isNotEmpty()) {
                        Icon(painter = painterResource(id = R.drawable.cross),
                            contentDescription = "Clear Icon",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.clickable { onTextChange("") })
                    }
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search)
        )
    }
}