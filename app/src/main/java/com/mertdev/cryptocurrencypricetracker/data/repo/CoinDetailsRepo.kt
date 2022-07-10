package com.mertdev.cryptocurrencypricetracker.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mertdev.cryptocurrencypricetracker.data.datasource.CoinPagingSource
import com.mertdev.cryptocurrencypricetracker.data.model.CoinItem
import com.mertdev.cryptocurrencypricetracker.service.ApiService
import com.mertdev.cryptocurrencypricetracker.utils.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CoinDetailsRepo @Inject constructor(private val apiService: ApiService) {

}