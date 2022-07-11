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
import com.mertdev.cryptocurrencypricetracker.utils.DetailState
import com.mertdev.cryptocurrencypricetracker.utils.loadImageFromUrl
import com.mertdev.cryptocurrencypricetracker.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: DetailsFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)
        collectDetailsData()

        binding.swipeRefreshLayout.setOnRefreshListener {
            collectDetailsData()
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun collectDetailsData(){
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect{ uiState ->
                when (uiState){
                    is DetailState.Loading -> binding.swipeRefreshLayout.isRefreshing = true
                    is DetailState.Empty -> isEmpty()
                    is DetailState.Failure -> isFailure()
                    is DetailState.Success -> isSuccess(uiState.data)
                }
            }
        }
    }

    private fun isSuccess(details: CoinDetails){
        binding.swipeRefreshLayout.isRefreshing = false
        binding.titleTxt.text = details.name.plus(" "+details.symbol?.uppercase())
        details.hashingAlgorithm?.let { binding.hashingAlg.text = it }
        binding.describeTxt.text = details.description.toString()
        binding.currentPriceTxt.text = details.marketData?.currentPrice?.usd.toString()
        details.image?.large?.let { binding.coinImg.loadImageFromUrl(it) }
        binding.percentTxt.text = details.marketData?.priceChangePercentage24h?.toString()
    }

    private fun isFailure(){
        binding.swipeRefreshLayout.isRefreshing = false
        requireContext().showToast("Failed to fetch data")
    }

    private fun isEmpty(){
        binding.swipeRefreshLayout.isRefreshing = false
        requireContext().showToast("No data here it's empty")
    }

}