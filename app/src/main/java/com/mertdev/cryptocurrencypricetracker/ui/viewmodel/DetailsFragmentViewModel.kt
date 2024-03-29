package com.mertdev.cryptocurrencypricetracker.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertdev.cryptocurrencypricetracker.data.model.CoinDetails
import com.mertdev.cryptocurrencypricetracker.data.model.CoinItem
import com.mertdev.cryptocurrencypricetracker.data.repo.CoinDetailsRepo
import com.mertdev.cryptocurrencypricetracker.data.repo.DataStoreRepo
import com.mertdev.cryptocurrencypricetracker.data.repo.FirebaseRepo
import com.mertdev.cryptocurrencypricetracker.utils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsFragmentViewModel @Inject constructor(
    private val coinDetailsRepo: CoinDetailsRepo,
    private val firebaseRepo: FirebaseRepo,
    private val dataStoreRepo: DataStoreRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _detailState = MutableStateFlow<DataStatus<CoinDetails>>(DataStatus.Loading())
    val detailState: StateFlow<DataStatus<CoinDetails>> get() = _detailState

    private val _favoriteState = MutableStateFlow<DataStatus<Boolean>>(DataStatus.Loading())
    val favoriteState: StateFlow<DataStatus<Boolean>> get() = _favoriteState

    private val coinId = savedStateHandle.get<String>("id")

    val readFromDataStore = dataStoreRepo.readFromDataStore

    init {
        getCoinDetails()
        isFavorite()
    }

    fun saveToDataStore(interval: String) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepo.saveToDataStore(interval)
        }

    fun getCoinDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            if (coinId != null) {
                coinDetailsRepo.getCoinDetails(coinId)
                    .catch { exception ->
                        _detailState.value = DataStatus.Error(exception.message.toString())
                    }.collect { data ->
                        _detailState.value = DataStatus.Success(data)
                    }
            } else {
                _detailState.value = DataStatus.Empty()
            }
        }
    }

    fun isFavorite() {
        if (coinId != null) {
            firebaseRepo.isFavorite(coinId)?.addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    _favoriteState.value = DataStatus.Success(true)
                } else {
                    _favoriteState.value = DataStatus.Empty()
                }
            }?.addOnFailureListener { exception ->
                _favoriteState.value = DataStatus.Error(exception.message)
            }
        } else {
            _favoriteState.value = DataStatus.Empty()
        }
    }

    fun addFavorite(coinItem: CoinItem) =
        coinId?.let {
            firebaseRepo.addFavorite(it, coinItem)
        }

    fun deleteFavorite() =
        coinId?.let {
            firebaseRepo.deleteFavorite(it)
        }

}