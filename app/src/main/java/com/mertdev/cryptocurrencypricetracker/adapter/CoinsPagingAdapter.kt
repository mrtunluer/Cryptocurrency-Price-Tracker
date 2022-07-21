package com.mertdev.cryptocurrencypricetracker.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mertdev.cryptocurrencypricetracker.data.model.CoinItem
import com.mertdev.cryptocurrencypricetracker.databinding.CoinsItemBinding
import com.mertdev.cryptocurrencypricetracker.utils.extensions.loadImageFromUrl

class CoinsPagingAdapter: PagingDataAdapter<CoinItem, CoinsPagingAdapter.ViewHolder>(Companion) {

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
            CoinsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val coin = getItem(position) ?: return

        val name = coin.name
        val symbol = coin.symbol?.uppercase()
        val price = coin.currentPrice.toString()
        val imageUrl = coin.image.toString()

        holder.apply {
            binding.coinImg.loadImageFromUrl(imageUrl)
            binding.coinText.text = name.plus(" $symbol")
            binding.priceTxt.text = price

            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(coin)
                }
            }
        }
    }

    inner class ViewHolder(val binding: CoinsItemBinding):
        RecyclerView.ViewHolder(binding.root)

    private var onItemClickListener: ((CoinItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (CoinItem) -> Unit) { onItemClickListener = listener }

}