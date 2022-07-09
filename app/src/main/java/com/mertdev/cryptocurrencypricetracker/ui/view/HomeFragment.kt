package com.mertdev.cryptocurrencypricetracker.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.mertdev.cryptocurrencypricetracker.R
import com.mertdev.cryptocurrencypricetracker.adapter.CoinsPagingAdapter
import com.mertdev.cryptocurrencypricetracker.adapter.FooterAdapter
import com.mertdev.cryptocurrencypricetracker.data.model.CoinItem
import com.mertdev.cryptocurrencypricetracker.databinding.FragmentHomeBinding
import com.mertdev.cryptocurrencypricetracker.ui.viewmodel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeFragmentViewModel by viewModels()
    private val coinsPagingAdapter = CoinsPagingAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        initRv()
        observeCoin()
        loadStateListener()

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getCoins()
        }

    }

    private fun loadStateListener(){
        coinsPagingAdapter.addLoadStateListener { combinedLoadStates ->
            binding.swipeRefreshLayout.isRefreshing = combinedLoadStates.source.refresh is LoadState.Loading
            binding.errorTxt.isVisible = combinedLoadStates.source.refresh is LoadState.Error
        }
    }

    private fun initRv(){
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = coinsPagingAdapter.withLoadStateFooter(FooterAdapter{
                coinsPagingAdapter.retry() })
        }
    }

    private fun submitData(pagingData: PagingData<CoinItem>){
        lifecycleScope.launch{
            coinsPagingAdapter.submitData(pagingData)
        }
    }

    private fun observeCoin(){
        viewModel.coinLiveData.observe(viewLifecycleOwner){ coinItem ->
            coinItem?.let { submitData(coinItem) }
        }
    }

}
