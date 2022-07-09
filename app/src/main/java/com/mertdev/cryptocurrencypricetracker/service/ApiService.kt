package com.mertdev.cryptocurrencypricetracker.service

import com.mertdev.cryptocurrencypricetracker.data.model.Coin
import com.mertdev.cryptocurrencypricetracker.data.model.CoinItem
import com.mertdev.cryptocurrencypricetracker.utils.Constants.COINS
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.Flow

interface ApiService {

    @GET(COINS)
    suspend fun getCoins(
        @Query("vs_currency")vs_currency: String,
        @Query("sparkline")sparkline: Boolean,
        @Query("page")page: Int
    ): Coin

}