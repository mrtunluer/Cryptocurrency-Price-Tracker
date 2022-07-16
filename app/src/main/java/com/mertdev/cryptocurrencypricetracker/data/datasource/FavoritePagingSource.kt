package com.mertdev.cryptocurrencypricetracker.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.QuerySnapshot
import com.mertdev.cryptocurrencypricetracker.data.model.CoinItem
import com.mertdev.cryptocurrencypricetracker.data.repo.FirebaseRepo
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritePagingSource @Inject constructor(
    private val firebaseRepo: FirebaseRepo
    ) : PagingSource<QuerySnapshot, CoinItem>() {

    override fun getRefreshKey(state: PagingState<QuerySnapshot, CoinItem>): QuerySnapshot? {
        return null
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, CoinItem> {
        return try {
            val currentPage = params.key ?: firebaseRepo.getFavorites()!!.get().await()
            val lastVisibleCoin = currentPage.documents[currentPage.size() - 1]
            val nextPage = firebaseRepo.getFavorites()!!.startAfter(lastVisibleCoin).get().await()
            LoadResult.Page(
                data = currentPage.toObjects(CoinItem::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}