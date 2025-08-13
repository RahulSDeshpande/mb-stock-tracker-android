package com.rahul.stocker

import com.rahul.stocker.data.remote.StockPriceService
import com.rahul.stocker.data.repository.StocksPriceRepositoryImpl
import com.rahul.stocker.domain.model.StockPriceEventModel
import com.rahul.stocker.ext.PRICE_REFRESH_INTERVAL_MILLIS
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StocksPriceRepositoryTest {
    @Test
    fun verifyStocksUpdateOnReceivingEcho() =
        runTest {
            val isConnected = MutableStateFlow(false)
            val receivingEvent = MutableSharedFlow<StockPriceEventModel>(replay = 1)

            val stockPriceService = mockk<StockPriceService>(relaxed = true)

            every { stockPriceService.isConnected } returns isConnected
            every { stockPriceService.receivingEvent } returns receivingEvent

            val symbols = listOf("AAA", "BBB")

            val repository =
                StocksPriceRepositoryImpl(
                    symbols = symbols,
                    stockPriceService = stockPriceService,
                )
            repository.setRefreshInterval(1)

            // Start repository with test scope and emit an incoming echoed price
            repository.start(this)

            receivingEvent.tryEmit(
                StockPriceEventModel(
                    symbol = "AAA",
                    price = 123.45,
                    timestamp = System.currentTimeMillis(),
                ),
            )

            val stocks =
                withTimeout(PRICE_REFRESH_INTERVAL_MILLIS) {
                    repository.stocks.first { stocks ->
                        stocks.any { it.symbol == "AAA" && it.price == 123.45 }
                    }
                }

            val updated = stocks.first { it.symbol == "AAA" }

            assertEquals(123.45, updated.price, 0.0)
            assertTrue(updated.previousPrice != null)

            // Repository may connect in start(); not strictly required here
            verify(atLeast = 1) { stockPriceService.connect(any()) }

            repository.stop()
        }
}
