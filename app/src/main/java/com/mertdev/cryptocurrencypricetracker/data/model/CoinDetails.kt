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
    val marketData: MarketData?,
    @SerializedName("image")
    val image: Image?
)

data class Image(
    @SerializedName("large")
    val large: String?
)

data class MarketData(
    @SerializedName("current_price")
    val currentPrice: CurrentPrice?,
    @SerializedName("price_change_percentage_24h")
    val priceChangePercentage24h: Double?
)

data class CurrentPrice(
    @SerializedName("usd")
    val usd: Double?
)

data class Description(
    @SerializedName("en")
    val en: String?
)
