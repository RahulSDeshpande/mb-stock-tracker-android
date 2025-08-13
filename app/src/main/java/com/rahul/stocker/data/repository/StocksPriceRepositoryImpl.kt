package com.rahul.stocker.data.repository

import com.rahul.stocker.data.remote.StockPriceService
import com.rahul.stocker.domain.model.StockModel
import com.rahul.stocker.domain.model.StockPriceEventModel
import com.rahul.stocker.domain.repository.StocksRepository
import com.rahul.stocker.ext.PRICE_REFRESH_INTERVAL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
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
        private val ownScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        private var externalScope: CoroutineScope? = null

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

        init {
            // Default to internal scope until a ViewModel scope is provided via start()
            ownScope.launch {
                stockPriceService.receivingEvent.collect { event ->
                    val symbol = event.symbol
                    val newPrice = event.price
                    val now = event.timestamp
                    val current = stocksMap[symbol]

                    if (symbol.isNotBlank() && !newPrice.isNaN() && current != null) {
                        stocksMap[symbol] =
                            current.copy(
                                previousPrice = current.price,
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

        override fun start(scope: CoroutineScope) {
            externalScope = scope
            stockPriceService.connect(scope = scope)

            if (senderJob?.isActive == true) {
                return
            }

            senderJob =
                (externalScope ?: ownScope).launch {
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

                        val seconds = refreshInterval.value.coerceAtLeast(1)

                        delay(timeMillis = seconds * PRICE_REFRESH_INTERVAL)
                    }
                }
        }

        override fun stop() {
            senderJob?.cancel()
            senderJob = null
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
