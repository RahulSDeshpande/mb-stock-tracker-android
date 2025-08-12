package com.rahul.stocker.data.remote

import com.rahul.stocker.domain.model.StockPriceEventModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface StockPriceService {
    val isConnected: StateFlow<Boolean>

    fun connect()

    fun disconnect()

    val receivingEvent: SharedFlow<StockPriceEventModel>

    fun sendEvent(event: StockPriceEventModel)

    fun cancel()
}
