package com.rahul.stocker.fake

import com.rahul.stocker.data.remote.StockPriceService
import com.rahul.stocker.domain.model.StockPriceEventModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeStockPriceService : StockPriceService {
    override val isConnected = MutableStateFlow(false)

    override val receivingEvent =
        MutableSharedFlow<StockPriceEventModel>(
            replay = 1,
            extraBufferCapacity = 16,
        )

    override fun connect(scope: CoroutineScope) {
        isConnected.value = true
    }

    override fun sendEvent(event: StockPriceEventModel) {
        receivingEvent.tryEmit(event)
    }

    override fun disconnect() {
        isConnected.value = false
    }

    override fun cancel() {
        isConnected.value = false
    }
}
