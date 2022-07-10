package com.mertdev.cryptocurrencypricetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.mertdev.cryptocurrencypricetracker.data.repo.CoinRepo
import com.mertdev.cryptocurrencypricetracker.utils.CoinListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val coinRepo: CoinRepo
) : ViewModel(){

    private val _state = MutableStateFlow(CoinListState())
    val state: StateFlow<CoinListState> get() = _state

    init {
        getCoins()
    }

    fun getCoins() {
        viewModelScope.launch {
            coinRepo.getCoins().cachedIn(viewModelScope).collectLatest {
                _state.value = CoinListState(it)
            }
        }
    }

}