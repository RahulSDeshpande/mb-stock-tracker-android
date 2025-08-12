package com.rahul.stocker.ext

import com.rahul.stocker.domain.model.StockModel

const val PRICE_REFRESH_INTERVAL = 1000L

val stockSymbols =
    listOf(
        "AAPL",
        "GOOG",
        "TSLA",
        "AMZN",
        "MSFT",
        "NVDA",
        "META",
        "NFLX",
        "AMD",
        "INTC",
        "ORCL",
        "ADBE",
        "CRM",
        "UBER",
        "LYFT",
        "SHOP",
        "SQ",
        "PYPL",
        "PLTR",
        "SNOW",
        "BABA",
        "V",
        "MA",
        "DIS",
        "NKE",
    )

val mockedStocks =
    listOf(
        StockModel(
            symbol = "AAPL",
            price = 200.00,
            previousPrice = 200.00,
            lastChangedTimestamp = null,
        ),
        StockModel(
            symbol = "TSLA",
            price = 182.10,
            previousPrice = 185.55,
            lastChangedTimestamp = System.currentTimeMillis(),
        ),
        StockModel(
            symbol = "AMD",
            price = 156.42,
            previousPrice = 153.11,
            lastChangedTimestamp = System.currentTimeMillis(),
        ),
        StockModel(
            symbol = "NVDA",
            price = 950.12,
            previousPrice = 948.33,
            lastChangedTimestamp = System.currentTimeMillis(),
        ),
        StockModel(
            symbol = "MSFT",
            price = 428.00,
            previousPrice = 430.10,
            lastChangedTimestamp = System.currentTimeMillis(),
        ),
        StockModel(
            symbol = "AMZN",
            price = 189.34,
            previousPrice = 186.22,
            lastChangedTimestamp = System.currentTimeMillis(),
        ),
        StockModel(
            symbol = "GOOG",
            price = 172.55,
            previousPrice = 172.55,
            lastChangedTimestamp = null,
        ),
        StockModel(
            symbol = "META",
            price = 516.72,
            previousPrice = 510.04,
            lastChangedTimestamp = System.currentTimeMillis(),
        ),
    )
