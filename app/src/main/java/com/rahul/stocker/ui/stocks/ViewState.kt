package com.rahul.stocker.ui.stocks

import com.rahul.stocker.domain.model.StockModel

data class ViewState(
    val isConnected: Boolean = false,
    val isRunning: Boolean = false,
    val stocks: List<StockModel> = emptyList(),
)
