package com.rahul.stocker.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahul.stocker.R
import com.rahul.stocker.ext.EnumAppTheme
import com.rahul.stocker.ext.EnumBottomTab
import com.rahul.stocker.ext.theme.AppTypography
import com.rahul.stocker.ui.settings.SettingsScreen
import com.rahul.stocker.ui.stocks.StocksScreen
import com.rahul.stocker.ui.stocks.StocksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val vm: StocksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
    }

    private fun initViews() {
        setContent {
            val viewState by vm.viewStateEvent.collectAsStateWithLifecycle()

            val useDark = viewState.appTheme == EnumAppTheme.DARK

            MaterialTheme(
                colorScheme =
                    if (useDark) {
                        darkColorScheme()
                    } else {
                        lightColorScheme()
                    },
                typography = AppTypography,
            ) {
                Scaffold(
                    bottomBar = {
                        NavigationBar(
                            modifier = Modifier.shadow(16.dp),
                            containerColor = MaterialTheme.colorScheme.surface,
                        ) {
                            NavigationBarItem(
                                selected = viewState.selectedTab == EnumBottomTab.STOCKS,
                                onClick = { vm.selectTab(EnumBottomTab.STOCKS) },
                                label = {
                                    Text(
                                        text = getString(R.string.tab_stocks),
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                                    )
                                },
                                icon = {
                                    Image(
                                        painter = painterResource(R.drawable.ic_stocks),
                                        contentDescription = "Stocks",
                                    )
                                },
                            )
                            NavigationBarItem(
                                selected = viewState.selectedTab == EnumBottomTab.SETTINGS,
                                onClick = { vm.selectTab(EnumBottomTab.SETTINGS) },
                                label = {
                                    Text(
                                        text = getString(R.string.tab_settings),
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                                    )
                                },
                                icon = {
                                    Image(
                                        painter = painterResource(R.drawable.ic_settings),
                                        contentDescription = "Settings",
                                    )
                                },
                            )
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.background,
                ) { padding ->
                    when (viewState.selectedTab) {
                        EnumBottomTab.STOCKS ->
                            StocksScreen(
                                viewState = viewState,
                                onSwitched = { vm.switch() },
                            )

                        EnumBottomTab.SETTINGS ->
                            SettingsScreen(
                                intervalSeconds = viewState.refreshInterval,
                                theme = viewState.appTheme,
                                onIntervalChange = vm::setRefreshInterval,
                                onThemeChange = vm::setAppTheme,
                            )
                    }
                }
            }
        }
    }
}
