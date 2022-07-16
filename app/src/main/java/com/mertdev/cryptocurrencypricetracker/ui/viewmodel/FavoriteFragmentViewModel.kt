package com.mertdev.cryptocurrencypricetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.mertdev.cryptocurrencypricetracker.data.repo.FirebaseRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteFragmentViewModel @Inject constructor(
    private val firebaseRepo: FirebaseRepo
) : ViewModel(){

    fun getFavoriteCoins() = firebaseRepo.getFavorites()

}