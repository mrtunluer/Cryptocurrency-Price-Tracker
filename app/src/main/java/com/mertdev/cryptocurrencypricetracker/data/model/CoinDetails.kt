package com.mertdev.cryptocurrencypricetracker.data.model


import com.google.gson.annotations.SerializedName

data class CoinDetails(
    @SerializedName("id")
    val id: String?,
    @SerializedName("description")
    val description: Description?,
    @SerializedName("hashing_algorithm")
    val hashingAlgorithm: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("symbol")
    val symbol: String?,
    @SerializedName("market_data")
    val marketData: MarketData?
)

data class MarketData(
    @SerializedName("current_price")
    val currentPrice: CurrentPrice?
)

data class CurrentPrice(
    @SerializedName("usd")
    val usd: Double?
)

data class Description(
    @SerializedName("en")
    val en: String?
)
