package com.rahul.stocker

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.rahul.stocker.domain.model.StockModel
import com.rahul.stocker.ui.stocks.StocksScreen
import com.rahul.stocker.ui.stocks.ViewState
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class StocksScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun verifyAppBarTitleAndStockListVisible() {
        val stocks =
            listOf(
                StockModel(
                    symbol = "AAPL",
                    price = 201.25,
                    previousPrice = 199.80,
                    lastChangedTimestamp = System.currentTimeMillis(),
                ),
                StockModel(
                    symbol = "TSLA",
                    price = 182.10,
                    previousPrice = 185.55,
                    lastChangedTimestamp = System.currentTimeMillis(),
                ),
            )

        composeRule.setContent {
            MaterialTheme {
                StocksScreen(
                    viewState =
                        ViewState(
                            isConnected = true,
                            isUpdating = false,
                            stocks = stocks,
                        ),
                )
            }
        }

        composeRule.apply {
            onNodeWithText("Stocker").assertIsDisplayed()
            // Verify Switch exists and is OFF when isUpdating is false
            onNode(
                SemanticsMatcher.expectValue(
                    key = SemanticsProperties.Role,
                    expectedValue = Role.Switch,
                ),
            ).assertIsOff()
            onNodeWithText("AAPL").assertIsDisplayed()
            onNodeWithText("TSLA").assertIsDisplayed()
        }
    }

    @Test
    fun verifySwitchStateWhenUpdating() {
        composeRule.setContent {
            MaterialTheme {
                StocksScreen(
                    viewState =
                        ViewState(
                            isConnected = false,
                            isUpdating = true,
                            stocks = emptyList(),
                        ),
                )
            }
        }

        // Verify Switch is ON when isUpdating is true
        composeRule
            .onNode(
                SemanticsMatcher.expectValue(
                    key = SemanticsProperties.Role,
                    expectedValue = Role.Switch,
                ),
            ).assertIsOn()
    }

    @Test
    fun verifyRedDotWhenNotConnected() {
        val stocks = emptyList<StockModel>()

        // Connected = false → 🔴
        composeRule.setContent {
            MaterialTheme {
                StocksScreen(
                    viewState =
                        ViewState(
                            isConnected = false,
                            isUpdating = false,
                            stocks = stocks,
                        ),
                )
            }
        }

        composeRule
            .onNodeWithText("🔴")
            .assertIsDisplayed()
    }

    @Test
    fun verifyGreenDotWhenConnected() {
        val stocks = emptyList<StockModel>()

        // Connected = true → 🟢
        composeRule.setContent {
            MaterialTheme {
                StocksScreen(
                    viewState =
                        ViewState(
                            isConnected = true,
                            isUpdating = false,
                            stocks = stocks,
                        ),
                )
            }
        }

        composeRule
            .onNodeWithText("🟢")
            .assertIsDisplayed()
    }

    @Test
    fun verifySwitchTriggersCallbackOnClick() {
        var isUpdating = false

        composeRule.setContent {
            MaterialTheme {
                StocksScreen(
                    viewState =
                        ViewState(
                            isConnected = false,
                            isUpdating = false,
                            stocks = emptyList(),
                        ),
                    onSwitched = { isUpdating = true },
                )
            }
        }

        composeRule
            .onNode(
                SemanticsMatcher.expectValue(
                    key = SemanticsProperties.Role,
                    expectedValue = Role.Switch,
                ),
            ).performClick()

        assertTrue(isUpdating)
    }

    @Test
    fun verifyArrowUpdatesOnPriceChange() {
        val now = System.currentTimeMillis()

        val stocks =
            listOf(
                StockModel(
                    "UP",
                    price = 10.0,
                    previousPrice = 9.0,
                    lastChangedTimestamp = now,
                ),
                StockModel(
                    "DOWN",
                    price = 8.0,
                    previousPrice = 9.0,
                    lastChangedTimestamp = now,
                ),
                StockModel(
                    "FLAT",
                    price = 9.0,
                    previousPrice = 9.0,
                    lastChangedTimestamp = now,
                ),
            )
        composeRule.setContent {
            MaterialTheme {
                StocksScreen(
                    viewState =
                        ViewState(
                            isConnected = true,
                            isUpdating = false,
                            stocks = stocks,
                        ),
                )
            }
        }

        composeRule.apply {
            onNodeWithText("↑").assertIsDisplayed()
            onNodeWithText("↓").assertIsDisplayed()
            onNodeWithText("-").assertIsDisplayed()
        }
    }
}
