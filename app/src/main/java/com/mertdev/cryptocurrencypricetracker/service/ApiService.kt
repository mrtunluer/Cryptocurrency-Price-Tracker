package com.mertdev.cryptocurrencypricetracker.service

import com.mertdev.cryptocurrencypricetracker.data.model.Coin
import com.mertdev.cryptocurrencypricetracker.data.model.CoinDetails
import com.mertdev.cryptocurrencypricetracker.utils.Constants.COINS
import com.mertdev.cryptocurrencypricetracker.utils.Constants.COIN_DETAILS
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET(COINS)
    suspend fun getCoins(
        @Query("vs_currency")vs_currency: String,
        @Query("page")page: Int
    ): Coin

    @GET(COIN_DETAILS)
    suspend fun getCoinDetails(
        @Path("id")id: String
    ): CoinDetails


}