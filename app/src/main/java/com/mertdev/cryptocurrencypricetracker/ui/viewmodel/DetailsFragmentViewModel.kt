package com.mertdev.cryptocurrencypricetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.mertdev.cryptocurrencypricetracker.data.repo.CoinDetailsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsFragmentViewModel @Inject constructor(
    private val coinDetailsRepo: CoinDetailsRepo
) : ViewModel(){

}