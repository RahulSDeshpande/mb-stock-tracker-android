package com.rahul.stocker.domain.model

data class StockPriceEventModel(
    val symbol: String,
    val price: Double,
    val timestamp: Long,
)
