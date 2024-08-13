package com.example.playlistmaker.settings.ui.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.example.playlistmaker.settings.util.ActionType

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val themeSettings by viewModel.themeSettingsState.observeAsState()

    val darkThemeEnabled = themeSettings?.darkMode ?: false

    SettingsScreenContent(
        darkThemeEnabled = darkThemeEnabled,
        onThemeSwitch = { viewModel.execute(ActionType.Theme(it)) },
        onShareApp = { viewModel.execute(ActionType.Share) },
        onSupportEmail = { viewModel.execute(ActionType.Support) },
        onOpenAgreement = { viewModel.execute(ActionType.Term) }
    )
}

@Composable
fun SettingsScreenContent(
    darkThemeEnabled: Boolean,
    onThemeSwitch: (Boolean) -> Unit,
    onShareApp: () -> Unit,
    onSupportEmail: () -> Unit,
    onOpenAgreement: () -> Unit
) {
    AppTheme(darkThemeEnabled) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(16.dp)
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.settings_button),
                        color = MaterialTheme.colors.onBackground,
                        fontSize = 22.sp
                    )
                },
                backgroundColor = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxWidth(),
                elevation = 0.dp
            )

            SettingsItem(
                text = stringResource(id = R.string.them_setting),
                trailingContent = {
                    Switch(
                        checked = darkThemeEnabled,
                        onCheckedChange = onThemeSwitch,
                        colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colors.primary)
                    )
                }
            )

            SettingsItem(
                text = stringResource(id = R.string.share_setting),
                trailingContent = {
                    IconButton(onClick = onShareApp) {
                        Icon(
                            painter = painterResource(id = R.drawable.share),
                            contentDescription = null,
                            tint = MaterialTheme.colors.onBackground
                        )
                    }
                }
            )

            SettingsItem(
                text = stringResource(id = R.string.support_setting),
                trailingContent = {
                    IconButton(onClick = onSupportEmail) {
                        Icon(
                            painter = painterResource(id = R.drawable.support),
                            contentDescription = null,
                            tint = MaterialTheme.colors.onBackground
                        )
                    }
                }
            )

            SettingsItem(
                text = stringResource(id = R.string.contract_button),
                trailingContent = {
                    IconButton(onClick = onOpenAgreement) {
                        Icon(
                            painter = painterResource(id = R.drawable.forward),
                            contentDescription = null,
                            tint = MaterialTheme.colors.onBackground
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun SettingsItem(
    text: String,
    trailingContent: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 21.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onBackground
        )
        trailingContent()
    }
}

@Composable
fun AppTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    MaterialTheme(
        colors = if (darkTheme) darkColors() else lightColors(),
        typography = Typography(
            body1 = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
        ),
        shapes = Shapes(),
        content = content
    )
}