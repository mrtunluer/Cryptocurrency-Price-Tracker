package com.mertdev.cryptocurrencypricetracker.data.model

import com.google.gson.annotations.SerializedName

data class CoinItem(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("current_price")
    val currentPrice: Double? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("symbol")
    val symbol: String? = null,
    @SerializedName("image")
    val image: String? = null
)