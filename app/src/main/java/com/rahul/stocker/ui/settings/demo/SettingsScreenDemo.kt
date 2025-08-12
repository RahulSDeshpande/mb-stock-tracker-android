package com.rahul.stocker.ui.settings.demo

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.rahul.stocker.ext.AppTheme
import com.rahul.stocker.ui.settings.SettingsScreen

@Preview(
    name = "Settings - Light",
    showBackground = true,
)
@Composable
private fun PreviewSettingsLight() {
    MaterialTheme {
        SettingsScreen(
            intervalSeconds = 5,
            theme = AppTheme.LIGHT,
            onIntervalChange = {},
            onThemeChange = {},
        )
    }
}

@Preview(
    name = "Settings - Dark",
    showBackground = true,
)
@Composable
private fun PreviewSettingsDark() {
    MaterialTheme {
        SettingsScreen(
            intervalSeconds = 3,
            theme = AppTheme.DARK,
            onIntervalChange = {},
            onThemeChange = {},
        )
    }
}
