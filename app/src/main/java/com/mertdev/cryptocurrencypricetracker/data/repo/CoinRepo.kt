package com.mertdev.cryptocurrencypricetracker.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mertdev.cryptocurrencypricetracker.data.datasource.CoinPagingSource
import com.mertdev.cryptocurrencypricetracker.data.model.CoinItem
import com.mertdev.cryptocurrencypricetracker.utils.Constants.COIN_PAGE_SIZE
import com.mertdev.cryptocurrencypricetracker.utils.Constants.PREFETCH_DISTANCE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CoinRepo @Inject constructor(private val coinPagingSource: CoinPagingSource) {
    fun getCoins(): Flow<PagingData<CoinItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = COIN_PAGE_SIZE,
                enablePlaceholders = false,
                prefetchDistance = PREFETCH_DISTANCE
            ),
            pagingSourceFactory = {coinPagingSource}
        ).flow
    }
}