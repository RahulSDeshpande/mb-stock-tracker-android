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
import com.rahul.stocker.domain.model.StockModel
import com.rahul.stocker.ui.stocks.StocksScreen
import com.rahul.stocker.ui.stocks.ViewState
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
}
