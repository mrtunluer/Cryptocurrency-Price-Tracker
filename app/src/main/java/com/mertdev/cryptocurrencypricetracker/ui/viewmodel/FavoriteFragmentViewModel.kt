package com.mertdev.cryptocurrencypricetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.mertdev.cryptocurrencypricetracker.data.repo.FavoritesRepo
import com.mertdev.cryptocurrencypricetracker.utils.CoinListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteFragmentViewModel @Inject constructor(
    private val favoritesRepo: FavoritesRepo
) : ViewModel(){

    private val _state = MutableStateFlow(CoinListState())
    val state: StateFlow<CoinListState> get() = _state

    init {
        getFavoriteCoins()
    }

    fun getFavoriteCoins() {
        viewModelScope.launch {
            favoritesRepo.getFavoriteCoins().cachedIn(viewModelScope).collectLatest {
                _state.value = CoinListState(it)
            }
        }
    }
}