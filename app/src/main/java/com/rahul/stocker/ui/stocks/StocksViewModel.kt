package com.rahul.stocker.ui.stocks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.stocker.domain.model.StockModel
import com.rahul.stocker.domain.repository.StocksRepository
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

        val viewStateEvent =
            combine(
                repository.isConnected,
                _isConnected,
                repository.stocks,
            ) { values: Array<Any?> ->
                val connected = values[0] as Boolean
                val running = values[1] as Boolean
                val stocks = values[2] as List<StockModel>

                ViewState(
                    isConnected = connected,
                    isRunning = running,
                    stocks = stocks,
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

        override fun onCleared() {
            repository.close()
            super.onCleared()
        }
    }
