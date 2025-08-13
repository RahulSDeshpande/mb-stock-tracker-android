package com.rahul.stocker.ui.stocks.demo

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.rahul.stocker.ext.mockedStocks
import com.rahul.stocker.ui.stocks.StockRow
import com.rahul.stocker.ui.stocks.StocksScreen
import com.rahul.stocker.ui.stocks.ViewState

@Preview(
    name = "Row - Up",
    showBackground = true,
)
@Composable
private fun PreviewStockRowUp() {
    MaterialTheme {
        StockRow(
            stock = mockedStocks[2],
            isUpdating = true,
        )
    }
}

@Preview(
    name = "Row - Down",
    showBackground = true,
)
@Composable
private fun PreviewStockRowDown() {
    MaterialTheme {
        StockRow(
            stock = mockedStocks[1],
            isUpdating = true,
        )
    }
}

@Preview(
    name = "Screen - Connected Running",
    showBackground = true,
)
@Composable
private fun PreviewStocksScreenConnected() {
    MaterialTheme {
        StocksScreen(
            viewState =
                ViewState(
                    isConnected = true,
                    isRunning = true,
                    stocks = mockedStocks.take(6),
                ),
        )
    }
}

@Preview(
    name = "Screen - Disconnected Stopped",
    showBackground = true,
)
@Composable
private fun PreviewStockScreenDisconnected() {
    MaterialTheme {
        StocksScreen(
            viewState =
                ViewState(
                    isConnected = false,
                    isRunning = false,
                    stocks = mockedStocks.asReversed().take(3),
                ),
        )
    }
}
