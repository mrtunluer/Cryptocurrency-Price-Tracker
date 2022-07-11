package com.mertdev.cryptocurrencypricetracker.utils

import com.mertdev.cryptocurrencypricetracker.data.model.CoinDetails

sealed class DataStatus {
    class Success(val data: CoinDetails): DataStatus()
    object Failure : DataStatus()
    object Loading: DataStatus()
    object Empty: DataStatus()
}
