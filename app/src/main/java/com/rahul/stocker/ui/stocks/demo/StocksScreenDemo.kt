package com.rahul.stocker.ui.stocks.demo

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.rahul.stocker.domain.model.StockModel
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
            stock = mockedStocks.random(),
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
            stock =
                StockModel(
                    symbol = "TSLA",
                    price = 182.10,
                    previousPrice = 185.55,
                    lastChangedTimestamp = System.currentTimeMillis(),
                ),
        )
    }
}

@Preview(
    name = "Screen - Connected Running",
    showBackground = true,
)
@Composable
private fun PreviewStocksScreenConnected() {
    val demoStocks =
        listOf(
            StockModel(
                symbol = "NVDA",
                price = 950.12,
                previousPrice = 948.33,
                lastChangedTimestamp = System.currentTimeMillis(),
            ),
            StockModel(
                symbol = "MSFT",
                price = 428.00,
                previousPrice = 430.10,
                lastChangedTimestamp = System.currentTimeMillis(),
            ),
            StockModel(
                symbol = "AMZN",
                price = 189.34,
                previousPrice = 186.22,
                lastChangedTimestamp = System.currentTimeMillis(),
            ),
            StockModel(
                symbol = "GOOG",
                price = 172.55,
                previousPrice = 172.55,
                lastChangedTimestamp = null,
            ),
            StockModel(
                symbol = "META",
                price = 516.72,
                previousPrice = 510.04,
                lastChangedTimestamp = System.currentTimeMillis(),
            ),
        )
    MaterialTheme {
        StocksScreen(
            viewState =
                ViewState(
                    isConnected = true,
                    isRunning = true,
                    stocks = demoStocks,
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
    val demoStocks =
        listOf(
            StockModel(
                symbol = "AAPL",
                price = 200.00,
                previousPrice = 200.00,
                lastChangedTimestamp = null,
            ),
            StockModel(
                symbol = "TSLA",
                price = 182.10,
                previousPrice = 185.55,
                lastChangedTimestamp = System.currentTimeMillis(),
            ),
            StockModel(
                symbol = "AMD",
                price = 156.42,
                previousPrice = 153.11,
                lastChangedTimestamp = System.currentTimeMillis(),
            ),
        )
    MaterialTheme {
        StocksScreen(
            viewState =
                ViewState(
                    isConnected = false,
                    isRunning = false,
                    stocks = demoStocks,
                ),
        )
    }
}
