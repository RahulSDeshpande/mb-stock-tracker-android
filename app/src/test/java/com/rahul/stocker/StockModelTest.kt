package com.rahul.stocker

import com.rahul.stocker.domain.model.StockModel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StockModelTest {
    @Test
    fun verifyStockPriceUpLogic() {
        val stockUp =
            StockModel(
                symbol = "AAA",
                price = 302.0,
                previousPrice = 300.0,
                lastChangedTimestamp = null,
            )

        assertTrue(stockUp.isPriceUp)
        assertFalse(stockUp.isPriceDown)
    }

    @Test
    fun verifyStockPriceDownLogic() {
        val stockDown =
            StockModel(
                symbol = "BBB",
                price = 300.0,
                previousPrice = 302.0,
                lastChangedTimestamp = null,
            )

        assertTrue(stockDown.isPriceDown)
        assertFalse(stockDown.isPriceUp)
    }

    @Test
    fun verifyStockPriceFlatLogic() {
        val stockFlat =
            StockModel(
                symbol = "CCC",
                price = 300.0,
                previousPrice = 300.0,
                lastChangedTimestamp = null,
            )

        assertFalse(stockFlat.isPriceUp)
        assertFalse(stockFlat.isPriceDown)
    }
}
