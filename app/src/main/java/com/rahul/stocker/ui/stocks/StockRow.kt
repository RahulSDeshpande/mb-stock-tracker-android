package com.rahul.stocker.ui.stocks

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rahul.stocker.R
import com.rahul.stocker.domain.model.StockModel
import com.rahul.stocker.ext.AppColors
import com.rahul.stocker.ext.PRICE_REFRESH_INTERVAL
import kotlinx.coroutines.delay

@Composable
fun StockRow(stock: StockModel) {
    var isUpdating by remember(stock.lastChangedTimestamp) { mutableStateOf(true) }

    LaunchedEffect(stock.lastChangedTimestamp) {
        if (stock.lastChangedTimestamp != null) {
            isUpdating = true
            delay(PRICE_REFRESH_INTERVAL)
            isUpdating = false
        } else {
            isUpdating = false
        }
    }

    val priceChangeBgColor =
        when {
            isUpdating && stock.isPriceUp -> AppColors.priceUpArrowColor
            isUpdating && stock.isPriceDown -> AppColors.priceDownArrowColor
            else -> AppColors.priceStillArrowColor
        }
    val bg by animateColorAsState(targetValue = priceChangeBgColor)

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(bg)
                .padding(
                    horizontal = 16.dp,
                    vertical = 16.dp,
                ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stock.symbol,
        )

        val priceChangeArrow =
            when {
                stock.isPriceUp -> stringResource(id = R.string.price_up)
                stock.isPriceDown -> stringResource(id = R.string.price_down)
                else -> stringResource(id = R.string.price_still)
            }

        val priceChangeArrowCoolor =
            when {
                stock.isPriceUp -> Color(0xFF14AD4A)
                stock.isPriceDown -> Color(0xFFD02D29)
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }
        Text(
            text = String.format(stringResource(id = R.string.price_format), stock.price),
            modifier = Modifier.widthIn(min = 40.dp),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = priceChangeArrow,
            color = priceChangeArrowCoolor,
        )
    }
}
