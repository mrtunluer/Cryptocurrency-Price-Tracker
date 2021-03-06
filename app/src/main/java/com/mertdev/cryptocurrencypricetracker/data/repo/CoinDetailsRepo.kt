package com.mertdev.cryptocurrencypricetracker.data.repo

import com.mertdev.cryptocurrencypricetracker.data.model.CoinDetails
import com.mertdev.cryptocurrencypricetracker.service.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CoinDetailsRepo @Inject constructor(private val apiService: ApiService) {
    fun getCoinDetails(id: String): Flow<CoinDetails>
    = flow {
        emit(apiService.getCoinDetails(id))
    }
}