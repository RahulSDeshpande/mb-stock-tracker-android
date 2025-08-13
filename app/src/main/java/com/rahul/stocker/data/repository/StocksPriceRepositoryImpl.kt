package com.rahul.stocker.data.repository

import com.rahul.stocker.data.remote.StockPriceService
import com.rahul.stocker.domain.model.StockModel
import com.rahul.stocker.domain.model.StockPriceEventModel
import com.rahul.stocker.domain.repository.StocksRepository
import com.rahul.stocker.ext.MIN_PRICE_REFRESH_INTERVAL_SECONDS
import com.rahul.stocker.ext.PRICE_REFRESH_INTERVAL_MILLIS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class StocksPriceRepositoryImpl
    @Inject
    constructor(
        private val symbols: List<String>,
        private val stockPriceService: StockPriceService,
    ) : StocksRepository {
        private val stocksMap: MutableMap<String, StockModel> =
            symbols
                .associateWith { symbol ->
                    StockModel(
                        symbol = symbol,
                        price = initialPrice(),
                        previousPrice = null,
                        lastChangedTimestamp = null,
                    )
                }.toMutableMap()

        private val _stocks = MutableStateFlow(stocksMap.values.sortedByDescending { it.price })
        override val stocks: StateFlow<List<StockModel>> = _stocks.asStateFlow()

        override val isConnected: StateFlow<Boolean> = stockPriceService.isConnected

        private var senderJob: Job? = null
        private var collectJob: Job? = null

        private val refreshInterval = MutableStateFlow(1)

        override fun start(scope: CoroutineScope) {
            stockPriceService.connect(scope = scope)

            if (senderJob?.isActive == true) {
                return
            }

            initReceivingEvents(scope = scope)

            initSendEvents(scope = scope)
        }

        private fun initReceivingEvents(scope: CoroutineScope) {
            if (collectJob?.isActive != true) {
                collectJob =
                    scope.launch {
                        stockPriceService.receivingEvent.collect { event ->
                            val symbol = event.symbol
                            val newPrice = event.price
                            val now = event.timestamp
                            val stock = stocksMap[symbol]

                            if (symbol.isNotBlank() && !newPrice.isNaN() && stock != null) {
                                stocksMap[symbol] =
                                    stock.copy(
                                        previousPrice = stock.price,
                                        price = newPrice,
                                        lastChangedTimestamp = now,
                                    )
                                _stocks.update {
                                    stocksMap.values.sortedByDescending {
                                        it.price
                                    }
                                }
                            }
                        }
                    }
            }
        }

        private fun initSendEvents(scope: CoroutineScope) {
            senderJob =
                scope.launch(Dispatchers.Default) {
                    while (isActive) {
                        val now = System.currentTimeMillis()

                        symbols.forEach { symbol ->
                            val current = stocksMap[symbol] ?: return@forEach
                            val nextPrice = nextPriceFrom(current = current.price)
                            val payload =
                                StockPriceEventModel(
                                    symbol = symbol,
                                    price = nextPrice,
                                    timestamp = now,
                                )

                            stockPriceService.sendEvent(event = payload)
                        }

                        val seconds =
                            refreshInterval.value.coerceAtLeast(
                                minimumValue = MIN_PRICE_REFRESH_INTERVAL_SECONDS,
                            )

                        delay(timeMillis = seconds * PRICE_REFRESH_INTERVAL_MILLIS)
                    }
                }
        }

        override fun stop() {
            senderJob?.cancel()
            senderJob = null

            collectJob?.cancel()
            collectJob = null

            // Reset change indicators so UI highlights clear when feed is stopped
            stocksMap.keys.forEach { key ->
                val stock = stocksMap[key] ?: return@forEach
                val updatedStock =
                    stock.copy(
                        previousPrice = stock.price,
                        lastChangedTimestamp = null,
                    )
                stocksMap[key] = updatedStock
            }
            _stocks.update { stocksMap.values.sortedByDescending { it.price } }

            stockPriceService.disconnect()
        }

        override fun close() {
            stop()
            stockPriceService.cancel()
        }

        override fun setRefreshInterval(seconds: Int) {
            refreshInterval.value = seconds.coerceAtLeast(1)
        }

        // TODO | MOVE TEMP HARD CODED VALUES TO Ext.kt
        private fun initialPrice(): Double = Random.Default.nextDouble(50.0, 500.0)

        // TODO | MOVE TEMP HARD CODED VALUES TO Ext.kt
        private fun nextPriceFrom(current: Double): Double {
            val delta = Random.Default.nextDouble(-3.0, 3.0)
            val next = (current + delta).coerceAtLeast(0.0)
            return String.format("%.2f", next).toDouble()
        }
    }
