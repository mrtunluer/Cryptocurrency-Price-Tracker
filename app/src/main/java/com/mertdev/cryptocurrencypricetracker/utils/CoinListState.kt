package com.mertdev.cryptocurrencypricetracker.utils

import androidx.paging.PagingData
import com.mertdev.cryptocurrencypricetracker.data.model.CoinItem

data class CoinListState(
    val coinListData: PagingData<CoinItem>? = PagingData.empty()
)