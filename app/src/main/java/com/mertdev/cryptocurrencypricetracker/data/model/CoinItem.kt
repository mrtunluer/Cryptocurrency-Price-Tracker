package com.mertdev.cryptocurrencypricetracker.data.model


import com.google.gson.annotations.SerializedName

data class CoinItem(
    @SerializedName("id")
    val id: String?,
    @SerializedName("current_price")
    val currentPrice: Double?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("symbol")
    val symbol: String?
)

