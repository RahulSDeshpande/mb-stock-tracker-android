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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rahul.stocker.R
import com.rahul.stocker.domain.model.StockModel
import com.rahul.stocker.ext.PRICE_REFRESH_INTERVAL_MILLIS
import com.rahul.stocker.ext.theme.AppColors
import kotlinx.coroutines.delay

@Composable
fun StockRow(
    stock: StockModel,
    // isUpdating: Boolean,
) {
    var isUpdating by remember(stock.lastChangedTimestamp) { mutableStateOf(true) }

    LaunchedEffect(stock.lastChangedTimestamp) {
        if (stock.lastChangedTimestamp != null) {
            isUpdating = true
            delay(PRICE_REFRESH_INTERVAL_MILLIS)
            isUpdating = false
        } else {
            isUpdating = false
        }
    }

    val priceChangeBgColor =
        when {
            isUpdating && stock.isPriceUp -> AppColors.priceUpBgColor
            isUpdating && stock.isPriceDown -> AppColors.priceDownBgColor
            else -> AppColors.priceStillBgColor
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
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
        )

        val priceChangeArrow =
            when {
                stock.isPriceUp -> stringResource(id = R.string.price_up)
                stock.isPriceDown -> stringResource(id = R.string.price_down)
                else -> stringResource(id = R.string.price_still)
            }

        val priceChangeArrowCoolor =
            when {
                stock.isPriceUp -> AppColors.priceUpArrowColor
                stock.isPriceDown -> AppColors.priceDownArrowColor
                else -> AppColors.priceStillArrowColor
            }
        Text(
            text =
                stringResource(
                    id = R.string.price_format,
                    stringResource(id = R.string.price_currency),
                    stock.price,
                ),
            modifier = Modifier.widthIn(min = 40.dp),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = priceChangeArrow,
            color = priceChangeArrowCoolor,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        )
    }
}
