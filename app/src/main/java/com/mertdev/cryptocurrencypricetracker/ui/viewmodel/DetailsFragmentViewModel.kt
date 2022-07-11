package com.mertdev.cryptocurrencypricetracker.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertdev.cryptocurrencypricetracker.data.repo.CoinDetailsRepo
import com.mertdev.cryptocurrencypricetracker.utils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsFragmentViewModel @Inject constructor(
    private val coinDetailsRepo: CoinDetailsRepo,
    savedStateHandle: SavedStateHandle
    ) : ViewModel(){

    private val _state = MutableStateFlow<DataStatus>(DataStatus.Empty)
    val state: StateFlow<DataStatus> get() = _state

    private val coinId = savedStateHandle.get<String>("id")

    init {
        getCoinDetails()
    }

    fun getCoinDetails(){
        viewModelScope.launch {
            coinId?.let {
                _state.value = DataStatus.Loading
                coinDetailsRepo.getCoinDetails(it)
                    .catch {
                        _state.value =  DataStatus.Failure
                    }.collect{ data ->
                        _state.value = DataStatus.Success(data)
                    }
            }
        }
    }

}