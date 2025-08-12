package com.rahul.stocker.ui.stocks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.stocker.domain.model.StockModel
import com.rahul.stocker.domain.repository.StocksRepository
import com.rahul.stocker.ext.EnumAppTheme
import com.rahul.stocker.ext.EnumBottomTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StocksViewModel
    @Inject
    constructor(
        private val repository: StocksRepository,
    ) : ViewModel() {
        private val _isConnected = MutableStateFlow(false)
        private val _refreshInterval = MutableStateFlow(2)
        private val _appTheme = MutableStateFlow(EnumAppTheme.LIGHT)
        private val _selectedTab = MutableStateFlow(EnumBottomTab.STOCKS)

        val viewStateEvent =
            combine(
                repository.isConnected,
                _isConnected,
                repository.stocks,
                _refreshInterval,
                _appTheme,
                _selectedTab,
            ) { values: Array<Any?> ->
                val connected = values[0] as Boolean
                val running = values[1] as Boolean
                val stocks = values[2] as List<StockModel>
                val interval = values[3] as Int
                val theme = values[4] as EnumAppTheme
                val tab = values[5] as EnumBottomTab

                ViewState(
                    isConnected = connected,
                    isRunning = running,
                    stocks = stocks,
                    refreshInterval = interval,
                    appTheme = theme,
                    selectedTab = tab,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = ViewState(),
            )

        fun switch() {
            val isConnectedNew = !_isConnected.value
            _isConnected.value = isConnectedNew

            if (isConnectedNew) {
                start()
            } else {
                stop()
            }
        }

        private fun start() {
            repository.start()
        }

        private fun stop() {
            repository.stop()
        }

        fun setRefreshInterval(seconds: Int) {
            val clamped = seconds.coerceAtLeast(1)
            _refreshInterval.value = clamped
            repository.setRefreshInterval(clamped)
        }

        fun setAppTheme(theme: EnumAppTheme) {
            _appTheme.value = theme
        }

        fun selectTab(tab: EnumBottomTab) {
            _selectedTab.value = tab
    }

        override fun onCleared() {
            repository.close()
            super.onCleared()
        }
    }
