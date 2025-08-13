package com.rahul.stocker

import com.rahul.stocker.data.remote.StockPriceService
import com.rahul.stocker.data.repository.StocksPriceRepositoryImpl
import com.rahul.stocker.domain.model.StockPriceEventModel
import com.rahul.stocker.ext.EnumAppTheme
import com.rahul.stocker.ext.EnumBottomTab
import com.rahul.stocker.ui.stocks.StocksViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class StocksViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun verifyAppThemeAndTabAndRefreshIntervalChanges() =
        runTest {
            val connection = MutableStateFlow(false)
            val incoming = MutableSharedFlow<StockPriceEventModel>(extraBufferCapacity = 16)

            val stockPriceService = mockk<StockPriceService>(relaxed = true)

            every { stockPriceService.isConnected } returns connection
            every { stockPriceService.receivingEvent } returns incoming

            val vm =
                StocksViewModel(
                    repository =
                        StocksPriceRepositoryImpl(
                            symbols = listOf("A"),
                            stockPriceService = stockPriceService,
                        ),
                )

            // Need to set necessary values to trigger state update
            vm.setAppTheme(EnumAppTheme.DARK)
            vm.setRefreshInterval(1)
            vm.selectTab(EnumBottomTab.SETTINGS)

            advanceUntilIdle()

            val viewState = vm.viewStateEvent.value

            assertEquals(EnumAppTheme.DARK, viewState.appTheme)
            assertEquals(1, viewState.refreshInterval)
            assertEquals(EnumBottomTab.SETTINGS, viewState.selectedTab)
        }
}
