package com.rahul.stocker.domain.repository

import com.rahul.stocker.domain.model.StockModel
import kotlinx.coroutines.flow.StateFlow

interface StocksRepository {
    val stocks: StateFlow<List<StockModel>>
    val isConnected: StateFlow<Boolean>

    fun start()

    fun stop()

    fun close()

    fun setRefreshInterval(seconds: Int)
}
