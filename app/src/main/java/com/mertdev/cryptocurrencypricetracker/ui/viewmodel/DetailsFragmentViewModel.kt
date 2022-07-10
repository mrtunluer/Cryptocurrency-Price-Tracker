package com.mertdev.cryptocurrencypricetracker.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertdev.cryptocurrencypricetracker.data.repo.CoinDetailsRepo
import com.mertdev.cryptocurrencypricetracker.utils.DetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsFragmentViewModel @Inject constructor(
    private val coinDetailsRepo: CoinDetailsRepo,
    private val savedStateHandle: SavedStateHandle
    ) : ViewModel(){

    private val _state = MutableStateFlow<DetailState>(DetailState.Empty)
    val state: StateFlow<DetailState> get() = _state

    private val coinId = savedStateHandle.get<String>("id")

    init {
        getCoinDetails()
    }

    fun getCoinDetails(){
        viewModelScope.launch {
            coinId?.let {
                _state.value = DetailState.Loading
                coinDetailsRepo.getCoinDetails(it)
                    .catch { error ->
                        _state.value =  DetailState.Failure(error)
                    }.collect{ data ->
                        _state.value = DetailState.Success(data)
                    }
            }
        }
    }

}