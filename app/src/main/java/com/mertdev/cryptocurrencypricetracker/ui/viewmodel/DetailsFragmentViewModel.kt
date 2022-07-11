package com.mertdev.cryptocurrencypricetracker.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertdev.cryptocurrencypricetracker.data.model.CoinDetails
import com.mertdev.cryptocurrencypricetracker.data.model.CoinItem
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
    private val firebaseRepo: FirebaseRepo,
    savedStateHandle: SavedStateHandle
    ) : ViewModel(){

    private val _detailState = MutableStateFlow<DataStatus<CoinDetails>>(DataStatus.Empty())
    val detailState: StateFlow<DataStatus<CoinDetails>> get() = _detailState

    private val _favoriteState = MutableStateFlow<DataStatus<CoinItem>>(DataStatus.Empty())
    val favoriteState: StateFlow<DataStatus<CoinItem>> get() = _favoriteState

    private val coinId = savedStateHandle.get<String>("id")

    init {
        getCoinDetails()
        getFavorite()
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

    fun getFavorite() {
        coinId?.let {
            _favoriteState.value = DataStatus.Loading()
            firebaseRepo.getFavorite(coinId)?.addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()){
                    val coinItem = documentSnapshot.toObject(CoinItem::class.java)
                    _favoriteState.value = DataStatus.Success(coinItem)
                }else{
                    _favoriteState.value = DataStatus.Empty()
                }
            }?.addOnFailureListener { exception ->
                _favoriteState.value = DataStatus.Error(exception.message)
            }
        }
    }

    fun addFavorite(coinId: String) =
        firebaseRepo.addFavorite(coinId)

    fun deleteFavorite(coinId: String) =
        firebaseRepo.deleteFavorite(coinId)

}