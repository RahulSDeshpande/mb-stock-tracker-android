package com.rahul.stocker.data.remote

import com.rahul.stocker.domain.model.StockPriceEventModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class StockPriceServiceImpl(
    override val receivingEvent: SharedFlow<StockPriceEventModel> = MutableSharedFlow(),
) : StockPriceService {
    override fun connect() {
        TODO("Not yet implemented")
    }

    override fun disconnect() {
        TODO("Not yet implemented")
    }

    override fun sendEvent(event: StockPriceEventModel) {
        TODO("Not yet implemented")
    }

    override fun cancel() {
        TODO("Not yet implemented")
    }
}
