package com.mertdev.cryptocurrencypricetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mertdev.cryptocurrencypricetracker.data.model.CoinItem
import com.mertdev.cryptocurrencypricetracker.data.repo.CoinRepo
import com.mertdev.cryptocurrencypricetracker.data.repo.FirebaseRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val coinRepo: CoinRepo
) : ViewModel(){

    fun getCoins(): Flow<PagingData<CoinItem>> {
        return coinRepo.getCoins().cachedIn(viewModelScope)
    }

}