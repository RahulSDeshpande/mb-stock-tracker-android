package com.rahul.stocker.domain.repository

import com.rahul.stocker.domain.model.StockModel
import kotlinx.coroutines.flow.StateFlow

interface StockRepository {
    val stocks: StateFlow<StockModel>
    val isConnected: StateFlow<Boolean>

    fun start()

    fun stop()

    fun close()

    fun setRefreshInterval(seconds: Int)
}
