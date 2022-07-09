package com.mertdev.cryptocurrencypricetracker.data.repo.paging

import androidx.paging.PagingSource
import com.mertdev.cryptocurrencypricetracker.service.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoinPagingSource @Inject constructor(
    private val apiService: ApiService
    ){

}