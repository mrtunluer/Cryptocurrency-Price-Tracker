package com.mertdev.cryptocurrencypricetracker.ui.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.mertdev.cryptocurrencypricetracker.R
import com.mertdev.cryptocurrencypricetracker.adapter.FavoriteCoinsPagingAdapter
import com.mertdev.cryptocurrencypricetracker.adapter.FooterAdapter
import com.mertdev.cryptocurrencypricetracker.databinding.FragmentFavoriteBinding
import com.mertdev.cryptocurrencypricetracker.ui.viewmodel.FavoriteFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel: FavoriteFragmentViewModel by viewModels()
    private val favoriteCoinsPagingAdapter = FavoriteCoinsPagingAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteBinding.bind(view)
        initRv()
        loadStateListener()

        viewLifecycleOwner.lifecycleScope.launch {
            collectFavoriteCoins()
        }

        favoriteCoinsPagingAdapter.setOnItemClickListener {
            val bundle = bundleOf("id" to it.id)
            findNavController().navigate(R.id.action_viewPagerFragment_to_detailsFragment, bundle)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getFavoriteCoins()
        }

    }

    private suspend fun collectFavoriteCoins(){
        viewModel.state.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collectLatest { coinItem ->
            coinItem.coinListData?.let { favoriteCoinsPagingAdapter.submitData(it) }
        }
    }

    private fun loadStateListener(){
        favoriteCoinsPagingAdapter.addLoadStateListener { combinedLoadStates ->
            binding.swipeRefreshLayout.isRefreshing = combinedLoadStates.source.refresh is LoadState.Loading
            binding.errorTxt.isVisible = combinedLoadStates.source.refresh is LoadState.Error
        }
    }

    private fun initRv(){

        binding.swipeRefreshLayout.setColorSchemeColors(Color.WHITE)
        binding.swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.BLACK)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            adapter = favoriteCoinsPagingAdapter.withLoadStateFooter(FooterAdapter{
                favoriteCoinsPagingAdapter.retry() })
        }

    }

}