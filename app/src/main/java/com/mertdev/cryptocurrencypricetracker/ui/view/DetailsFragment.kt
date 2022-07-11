package com.mertdev.cryptocurrencypricetracker.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mertdev.cryptocurrencypricetracker.R
import com.mertdev.cryptocurrencypricetracker.data.model.CoinDetails
import com.mertdev.cryptocurrencypricetracker.databinding.FragmentDetailsBinding
import com.mertdev.cryptocurrencypricetracker.ui.viewmodel.DetailsFragmentViewModel
import com.mertdev.cryptocurrencypricetracker.utils.DataStatus
import com.mertdev.cryptocurrencypricetracker.utils.loadImageFromUrl
import com.mertdev.cryptocurrencypricetracker.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: DetailsFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)

        collectDetailsData()
        collectFavoriteData()

        binding.swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.getCoinDetails()
                viewModel.getFavorite()
            }
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.saveImg.setOnClickListener {

        }

    }

    private fun collectFavoriteData(){
        lifecycleScope.launchWhenStarted {
            viewModel.favoriteState.collect{ uiState ->
                when (uiState){
                    is DataStatus.Loading -> binding.swipeRefreshLayout.isRefreshing = true
                    is DataStatus.Empty -> emptyForFavoriteData()
                    is DataStatus.Error -> errorForFavoriteData()
                    is DataStatus.Success -> successForFavoriteData()
                }
            }
        }
    }

    private fun collectDetailsData(){
        lifecycleScope.launchWhenStarted {
            viewModel.detailState.collect{ uiState ->
                when (uiState){
                    is DataStatus.Loading -> binding.swipeRefreshLayout.isRefreshing = true
                    is DataStatus.Empty -> emptyForDetailsData()
                    is DataStatus.Error -> errorForDetailsData()
                    is DataStatus.Success -> uiState.data?.let { successForDetailsData(it) }
                }
            }
        }
    }

    private fun successForDetailsData(details: CoinDetails){
        binding.swipeRefreshLayout.isRefreshing = false
        binding.titleTxt.text = details.name.plus(" "+details.symbol?.uppercase())
        details.hashingAlgorithm?.let { binding.hashingAlg.text = it }
        binding.describeTxt.text = details.description.toString()

        details.marketData?.currentPrice?.usd?.let {
            binding.currentPriceTitleTxt.visibility = View.VISIBLE
            binding.currentPriceTxt.visibility = View.VISIBLE
            binding.currentPriceTxt.text = it.toString()
        }

        details.marketData?.priceChangePercentage24h?.let {
            binding.percentTitleTxt.visibility = View.VISIBLE
            binding.percentTxt.visibility = View.VISIBLE
            binding.percentTxt.text = it.toString()
        }
        details.image?.large?.let { binding.coinImg.loadImageFromUrl(it) }
    }

    private fun errorForDetailsData(){
        binding.swipeRefreshLayout.isRefreshing = false
        requireContext().showToast("Failed to fetch data")
    }

    private fun emptyForDetailsData(){
        binding.swipeRefreshLayout.isRefreshing = false
        requireContext().showToast("No data here it's empty")
    }

    private fun emptyForFavoriteData(){
        binding.swipeRefreshLayout.isRefreshing = false
        binding.saveImg.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
    }

    private fun errorForFavoriteData(){
        binding.swipeRefreshLayout.isRefreshing = false
        requireContext().showToast("Failed to fetch data")
    }

    private fun successForFavoriteData(){
        binding.swipeRefreshLayout.isRefreshing = false
        binding.saveImg.setImageResource(R.drawable.ic_baseline_bookmark_24)
    }

}