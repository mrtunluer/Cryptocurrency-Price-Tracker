package com.mertdev.cryptocurrencypricetracker.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.mertdev.cryptocurrencypricetracker.data.model.CoinItem
import com.mertdev.cryptocurrencypricetracker.data.repo.CoinRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val coinRepo: CoinRepo
) : ViewModel(){

    val coinLiveData: MutableLiveData<PagingData<CoinItem>> by lazy {
        MutableLiveData<PagingData<CoinItem>>()
    }

    init {
        getCoins()
    }

    fun getCoins() {
        viewModelScope.launch {
            coinRepo.getCoins().distinctUntilChanged().collectLatest {
                coinLiveData.value = it
            }
        }
    }

}