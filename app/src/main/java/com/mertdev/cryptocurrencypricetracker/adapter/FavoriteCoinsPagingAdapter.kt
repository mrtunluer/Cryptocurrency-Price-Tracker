package com.mertdev.cryptocurrencypricetracker.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mertdev.cryptocurrencypricetracker.data.model.CoinItem
import com.mertdev.cryptocurrencypricetracker.databinding.FavoriteItemBinding
import com.mertdev.cryptocurrencypricetracker.utils.loadImageFromUrl

class FavoriteCoinsPagingAdapter: PagingDataAdapter<CoinItem, FavoriteCoinsPagingAdapter.ViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<CoinItem>() {
        override fun areItemsTheSame(oldItem: CoinItem, newItem: CoinItem): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: CoinItem, newItem: CoinItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            FavoriteItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val coin = getItem(position) ?: return

        holder.apply {
            coin.image?.let { binding.image.loadImageFromUrl(it) }

            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(coin)
                }
            }

        }

    }

    inner class ViewHolder(val binding: FavoriteItemBinding):
        RecyclerView.ViewHolder(binding.root)

    private var onItemClickListener: ((CoinItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (CoinItem) -> Unit) { onItemClickListener = listener }

}