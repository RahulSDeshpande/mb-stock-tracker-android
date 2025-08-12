package com.rahul.stocker.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rahul.stocker.R
import com.rahul.stocker.ext.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    intervalSeconds: Int,
    theme: AppTheme,
    onIntervalChange: (Int) -> Unit,
    onThemeChange: (AppTheme) -> Unit,
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
                title = { Text(text = stringResource(id = R.string.settings_title)) },
            )
        },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(padding),
        ) {
            Text(
                text =
                    stringResource(
                        id = R.string.settings_refresh_interval,
                        intervalSeconds,
                    ),
            )

            Slider(
                value = intervalSeconds.toFloat(),
                onValueChange = { onIntervalChange(it.toInt().coerceIn(1, 10)) },
                valueRange = 1f..10f,
                steps = 8,
                modifier = Modifier.fillMaxWidth(),
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = stringResource(id = R.string.settings_theme_label),
                style = MaterialTheme.typography.titleMedium,
            )

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    RadioButton(
                        selected = theme == AppTheme.LIGHT,
                        onClick = { onThemeChange(AppTheme.LIGHT) },
                    )
                    Text(
                        text = stringResource(id = R.string.settings_theme_light),
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }

                Row(modifier = Modifier.weight(1f)) {
                    RadioButton(
                        selected = theme == AppTheme.DARK,
                        onClick = { onThemeChange(AppTheme.DARK) },
                    )
                    Text(
                        text = stringResource(id = R.string.settings_theme_dark),
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = stringResource(id = R.string.settings_developer_info_title),
                style = MaterialTheme.typography.titleMedium,
            )

            Text(text = stringResource(id = R.string.settings_developer_name))
        }
    }
}
