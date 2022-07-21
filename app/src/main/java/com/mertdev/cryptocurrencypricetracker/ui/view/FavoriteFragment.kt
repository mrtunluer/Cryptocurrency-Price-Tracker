package com.mertdev.cryptocurrencypricetracker.ui.view

import android.app.Dialog
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
import androidx.recyclerview.widget.GridLayoutManager
import com.mertdev.cryptocurrencypricetracker.R
import com.mertdev.cryptocurrencypricetracker.adapter.FavoriteCoinsAdapter
import com.mertdev.cryptocurrencypricetracker.data.model.CoinItem
import com.mertdev.cryptocurrencypricetracker.databinding.FragmentFavoriteBinding
import com.mertdev.cryptocurrencypricetracker.ui.viewmodel.FavoriteFragmentViewModel
import com.mertdev.cryptocurrencypricetracker.utils.Constants.SPAN_COUNT
import com.mertdev.cryptocurrencypricetracker.utils.DataStatus
import com.mertdev.cryptocurrencypricetracker.utils.extensions.initDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel: FavoriteFragmentViewModel by viewModels()
    private val favoriteCoinsAdapter = FavoriteCoinsAdapter()
    private lateinit var progress: Dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteBinding.bind(view)
        init()

        viewLifecycleOwner.lifecycleScope.launch {
            collectFavoriteCoins()
        }

        favoriteCoinsAdapter.setOnItemClickListener {
            val bundle = bundleOf("id" to it.id)
            findNavController().navigate(R.id.action_viewPagerFragment_to_detailsFragment, bundle)
        }

    }

    private suspend fun collectFavoriteCoins(){
        viewModel.getFavoriteCoins().flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collectLatest { state ->
            when (state) {
                is DataStatus.Loading -> progress.show()
                is DataStatus.Success -> state.data?.let { onSuccess(it) }
                is DataStatus.Error -> onError()
                is DataStatus.Empty -> onEmpty()
            }
        }
    }

    private fun onError(){
        binding.errorTxt.isVisible = true
        binding.noResultTxt.isVisible = false
        progress.dismiss()
    }

    private fun onSuccess(coinItemList: List<CoinItem>){
        binding.errorTxt.isVisible = false
        binding.noResultTxt.isVisible = false
        progress.dismiss()
        favoriteCoinsAdapter.submitList(coinItemList)
    }

    private fun onEmpty(){
        binding.errorTxt.isVisible = false
        binding.noResultTxt.isVisible = true
        favoriteCoinsAdapter.submitList(emptyList())
        progress.dismiss()
    }

    private fun init(){
        progress = Dialog(requireContext())
        progress.initDialog(R.layout.custom_progress, false)

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT, GridLayoutManager.VERTICAL, false)
            adapter = favoriteCoinsAdapter
        }
    }

}