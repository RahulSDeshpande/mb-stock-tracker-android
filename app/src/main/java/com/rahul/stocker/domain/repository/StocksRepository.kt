package com.rahul.stocker.domain.repository

import com.rahul.stocker.domain.model.StockModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface StocksRepository {
    val stocks: StateFlow<List<StockModel>>
    val isConnected: StateFlow<Boolean>

    fun start(scope: CoroutineScope)

    fun stop()

    fun close()

    fun setRefreshInterval(seconds: Int)
}
