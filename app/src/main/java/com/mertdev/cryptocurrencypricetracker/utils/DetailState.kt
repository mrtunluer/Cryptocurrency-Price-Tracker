package com.mertdev.cryptocurrencypricetracker.utils

import com.mertdev.cryptocurrencypricetracker.data.model.CoinDetails

sealed class DetailState {
    class Success(val data: CoinDetails): DetailState()
    class Failure(val message: Throwable): DetailState()
    object Loading: DetailState()
    object Empty: DetailState()
}