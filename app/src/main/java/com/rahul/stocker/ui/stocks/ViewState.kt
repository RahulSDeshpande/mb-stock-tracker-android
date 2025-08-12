package com.rahul.stocker.ui.stocks

import com.rahul.stocker.domain.model.StockModel
import com.rahul.stocker.ext.EnumAppTheme
import com.rahul.stocker.ext.EnumBottomTab

data class ViewState(
    val isConnected: Boolean = false,
    val isRunning: Boolean = false,
    val stocks: List<StockModel> = emptyList(),
    val refreshInterval: Int = 1,
    val appTheme: EnumAppTheme = EnumAppTheme.LIGHT,
    val selectedTab: EnumBottomTab = EnumBottomTab.STOCKS,
)
