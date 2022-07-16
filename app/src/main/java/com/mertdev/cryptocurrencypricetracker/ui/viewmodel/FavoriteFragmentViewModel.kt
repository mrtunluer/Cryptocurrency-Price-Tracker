package com.mertdev.cryptocurrencypricetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.mertdev.cryptocurrencypricetracker.data.datasource.FavoritePagingSource
import com.mertdev.cryptocurrencypricetracker.utils.Constants.FAVORITE_PAGE_SIZE
import com.mertdev.cryptocurrencypricetracker.utils.Constants.PREFETCH_DISTANCE
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteFragmentViewModel @Inject constructor(
    private val favoritePagingSource: FavoritePagingSource
) : ViewModel(){

    val favoriteCoins = Pager(
        config = PagingConfig(
            pageSize = FAVORITE_PAGE_SIZE,
            enablePlaceholders = false,
            prefetchDistance = PREFETCH_DISTANCE
        ),
        pagingSourceFactory = {favoritePagingSource}
    ).flow.cachedIn(viewModelScope)

}