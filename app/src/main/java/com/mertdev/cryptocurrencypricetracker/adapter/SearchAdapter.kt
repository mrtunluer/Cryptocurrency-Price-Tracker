package com.mertdev.cryptocurrencypricetracker.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mertdev.cryptocurrencypricetracker.data.model.CoinItem
import com.mertdev.cryptocurrencypricetracker.databinding.CoinsItemBinding
import com.mertdev.cryptocurrencypricetracker.utils.loadImageFromUrl

class SearchAdapter: RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<CoinItem>() {
        override fun areItemsTheSame(oldItem: CoinItem, newItem: CoinItem): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: CoinItem, newItem: CoinItem): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, differCallback)

    fun submitList(list: List<CoinItem>) {
        differ.submitList(list)
    }

    private fun getItem(position: Int): CoinItem {
        return differ.currentList[position]
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
        val coin = getItem(position)

        val name = coin.name
        val symbol = coin.symbol?.uppercase()
        val price = coin.currentPrice.toString()
        val imageUrl = coin.image.toString()

        holder.binding.coinImg.loadImageFromUrl(imageUrl)
        holder.binding.coinText.text = name.plus(" $symbol")
        holder.binding.priceTxt.text = price

        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(coin)
            }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ViewHolder(val binding: CoinsItemBinding):
        RecyclerView.ViewHolder(binding.root)

    private var onItemClickListener: ((CoinItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (CoinItem) -> Unit) { onItemClickListener = listener }

}