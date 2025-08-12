package com.rahul.stocker.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rahul.stocker.R
import com.rahul.stocker.ext.EnumAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    intervalSeconds: Int,
    theme: EnumAppTheme,
    onIntervalChange: (Int) -> Unit,
    onThemeChange: (EnumAppTheme) -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                modifier = Modifier.shadow(4.dp),
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                title = {
                    Text(
                        text = stringResource(id = R.string.settings_title),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    )
                },
            )
        },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(padding)
                    .padding(16.dp),
        ) {
            Text(
                text =
                    stringResource(
                        id = R.string.settings_refresh_interval,
                        intervalSeconds,
                    ),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
            )

            Slider(
                value = intervalSeconds.toFloat(),
                onValueChange = { onIntervalChange(it.toInt().coerceIn(1, 10)) },
                valueRange = 1f..10f,
                steps = 8,
                modifier = Modifier.fillMaxWidth(),
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 0.5.dp,
            )

            Text(
                text = stringResource(id = R.string.settings_theme_label),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
            )

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    RadioButton(
                        selected = theme == EnumAppTheme.LIGHT,
                        onClick = { onThemeChange(EnumAppTheme.LIGHT) },
                    )
                    Text(
                        text = stringResource(id = R.string.settings_theme_light),
                        modifier = Modifier.padding(start = 4.dp),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                    )
                }
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    RadioButton(
                        selected = theme == EnumAppTheme.DARK,
                        onClick = { onThemeChange(EnumAppTheme.DARK) },
                    )
                    Text(
                        text = stringResource(id = R.string.settings_theme_dark),
                        modifier = Modifier.padding(start = 4.dp),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 0.5.dp,
            )

            Text(
                text = stringResource(id = R.string.settings_developer_info_title),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
            )

            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = stringResource(id = R.string.settings_developer_name),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
            )
        }
    }
}
