package com.mertdev.cryptocurrencypricetracker.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertdev.cryptocurrencypricetracker.data.model.CoinDetails
import com.mertdev.cryptocurrencypricetracker.data.repo.CoinDetailsRepo
import com.mertdev.cryptocurrencypricetracker.data.repo.FirebaseRepo
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

    private val _detailState = MutableStateFlow<DataStatus<CoinDetails>>(DataStatus.Empty())
    val detailState: StateFlow<DataStatus<CoinDetails>> get() = _detailState

    private val coinId = savedStateHandle.get<String>("id")

    init {
        getCoinDetails()
    }

    fun getCoinDetails(){
        viewModelScope.launch {
            coinId?.let {
                _detailState.value = DataStatus.Loading()
                coinDetailsRepo.getCoinDetails(it)
                    .catch { exception ->
                        _detailState.value =  DataStatus.Error(exception.message.toString())
                    }.collect{ data ->
                        _detailState.value = DataStatus.Success(data)
                    }
            }
        }
    }

}