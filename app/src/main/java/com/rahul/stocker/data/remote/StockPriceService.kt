package com.rahul.stocker.data.remote

import com.rahul.stocker.domain.model.StockPriceEventModel
import kotlinx.coroutines.flow.SharedFlow

interface StockPriceService {
    fun connect()

    fun disconnect()

    val receivingEvent: SharedFlow<StockPriceEventModel>

    fun sendEvent(event: StockPriceEventModel)

    fun cancel()
}
