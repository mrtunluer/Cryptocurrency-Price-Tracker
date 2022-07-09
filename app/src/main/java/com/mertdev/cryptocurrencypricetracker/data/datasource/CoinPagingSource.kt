package com.mertdev.cryptocurrencypricetracker.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mertdev.cryptocurrencypricetracker.data.model.CoinItem
import com.mertdev.cryptocurrencypricetracker.service.ApiService
import com.mertdev.cryptocurrencypricetracker.utils.Constants.NETWORK_PAGE_SIZE
import com.mertdev.cryptocurrencypricetracker.utils.Constants.SPARKLINE
import com.mertdev.cryptocurrencypricetracker.utils.Constants.VS_CURRENCY
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoinPagingSource @Inject constructor(
    private val apiService: ApiService
): PagingSource<Int, CoinItem>() {

    override fun getRefreshKey(state: PagingState<Int, CoinItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CoinItem> {
        val pageIndex = params.key ?: 1
        return try {
            val coinItem = apiService.getCoins(
                VS_CURRENCY,
                SPARKLINE,
                pageIndex
            )
            val nextKey =
                if (coinItem.isEmpty()) {
                    null
                } else {
                    pageIndex + (params.loadSize / NETWORK_PAGE_SIZE)
                }
            LoadResult.Page(
                data = coinItem,
                prevKey = if (pageIndex == 1) null else pageIndex,
                nextKey = nextKey
            )
        }catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

}