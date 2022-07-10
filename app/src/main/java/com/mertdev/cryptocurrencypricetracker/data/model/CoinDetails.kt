package com.mertdev.cryptocurrencypricetracker.data.model


import com.google.gson.annotations.SerializedName

data class CoinDetails(
    @SerializedName("id")
    val id: String,
    @SerializedName("description")
    val description: Description,
    @SerializedName("hashing_algorithm")
    val hashingAlgorithm: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("symbol")
    val symbol: String
)

data class Description(
    @SerializedName("en")
    val en: String
)
