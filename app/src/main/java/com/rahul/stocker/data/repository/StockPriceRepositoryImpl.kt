package com.rahul.stocker.data.repository

import com.rahul.stocker.domain.model.StockModel
import com.rahul.stocker.domain.repository.StockRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockPriceRepositoryImpl
    @Inject
    constructor(
        override val stocks: StateFlow<StockModel>,
        override val isConnected: StateFlow<Boolean>,
    ) : StockRepository {
        override fun start() {
            TODO("Not yet implemented")
        }

        override fun stop() {
            TODO("Not yet implemented")
        }

        override fun close() {
            TODO("Not yet implemented")
        }

        override fun setRefreshInterval(seconds: Int) {
            TODO("Not yet implemented")
        }
    }
