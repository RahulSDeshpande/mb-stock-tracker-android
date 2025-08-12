package com.rahul.stocker.ui.stocks

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rahul.stocker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StocksScreen(
    viewState: ViewState,
    onSwitched: () -> Unit = {},
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
                title = { Text(text = stringResource(id = R.string.app_name)) },
                actions = {
                    TextButton(onClick = onSwitched) {
                        Text(
                            if (viewState.isRunning) {
                                stringResource(id = R.string.action_stop)
                            } else {
                                stringResource(id = R.string.action_start)
                            },
                        )
                    }
                },
                navigationIcon = {
                    val dot =
                        if (viewState.isConnected) {
                            stringResource(id = R.string.status_connected_dot)
                        } else {
                            stringResource(id = R.string.status_disconnected_dot)
                        }
                    Text(text = dot, modifier = Modifier.padding(start = 16.dp))
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding),
        ) {
            items(items = viewState.stocks) { stock ->
                StockRow(stock = stock)
                HorizontalDivider(thickness = 0.5.dp)
            }
        }
    }
}
