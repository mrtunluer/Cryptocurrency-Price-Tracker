package com.mertdev.cryptocurrencypricetracker.utils

object Constants {
    const val BASE_URL = "https://api.coingecko.com/api/v3/"
    const val COINS = "coins/markets"
    const val COIN_DETAILS = "coins/{id}"
    const val VS_CURRENCY = "usd"
    const val NETWORK_PAGE_SIZE = 100
    const val PREFETCH_DISTANCE = 1
    const val NUM_TABS = 2
    const val INITIAL_INTERVAL = "30"
    const val DATA_STORE_NAME = "refresh_interval"
    val TABS_FRAGMENT_TEXT = arrayOf("COINS","FAVORITES")

}