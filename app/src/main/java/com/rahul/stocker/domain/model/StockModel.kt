package com.rahul.stocker.domain.model

data class StockModel(
    val symbol: String,
    val price: Double,
    val previousPrice: Double?,
    val lastChangedTimestamp: Long?,
) {
    val isPriceUp: Boolean
        get() = previousPrice != null && price > previousPrice

    val isPriceDown: Boolean
        get() = previousPrice != null && price < previousPrice
}
