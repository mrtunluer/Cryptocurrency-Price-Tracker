package com.mertdev.cryptocurrencypricetracker.ui.view

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.mertdev.cryptocurrencypricetracker.R
import com.mertdev.cryptocurrencypricetracker.adapter.CoinsPagingAdapter
import com.mertdev.cryptocurrencypricetracker.adapter.FooterAdapter
import com.mertdev.cryptocurrencypricetracker.adapter.SearchAdapter
import com.mertdev.cryptocurrencypricetracker.data.model.CoinItem
import com.mertdev.cryptocurrencypricetracker.databinding.FragmentHomeBinding
import com.mertdev.cryptocurrencypricetracker.ui.viewmodel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeFragmentViewModel by viewModels()
    private val coinsPagingAdapter = CoinsPagingAdapter()
    private val searchAdapter = SearchAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        initRv()
        lifecycleScope.launchWhenStarted {
            collectCoin()
        }
        loadStateListener()
        search()

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getCoins()
        }

        coinsPagingAdapter.setOnItemClickListener {
            val bundle = bundleOf("id" to it.id)
            findNavController().navigate(R.id.action_homeFragment_to_detailsFragment, bundle)
        }

        searchAdapter.setOnItemClickListener {
            val bundle = bundleOf("id" to it.id)
            findNavController().navigate(R.id.action_homeFragment_to_detailsFragment, bundle)
        }

    }

    private fun search(){
        binding.searchTxt.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do Nothing
            }

            override fun afterTextChanged(s: Editable?) {
                if (s == null || s.isEmpty()){
                    showPagingRv()
                }else if (s.toString().isNotEmpty()){
                    showSearchRv()
                    filter(s)
                }
            }

        })
    }

    private fun showSearchRv(){
        binding.searchRv.visibility = View.VISIBLE
        binding.pagingRv.visibility = View.GONE
    }

    private fun showPagingRv(){
        binding.searchRv.visibility = View.GONE
        binding.pagingRv.visibility = View.VISIBLE
    }

    private fun filter(text: Editable?){
        val filteredList = arrayListOf<CoinItem>()
        for(item in coinsPagingAdapter.snapshot().toMutableList()){

            item?.let { coinItem ->
                if (coinItem.name != null && coinItem.symbol != null){
                    if (coinItem.name.lowercase().contains(text.toString().lowercase())){
                        filteredList.add(item)
                    }else if (coinItem.symbol.lowercase().contains(text.toString().lowercase())){
                        filteredList.add(item)
                    }
                }
            }

        }
        searchAdapter.submitList(filteredList)
        isResultExist(filteredList.size)
    }

    private fun isResultExist(size: Int){
        if (size == 0){
            binding.noResultTxt.visibility = View.VISIBLE
        }else{
            binding.noResultTxt.visibility = View.GONE
        }
    }

    private fun loadStateListener(){
        coinsPagingAdapter.addLoadStateListener { combinedLoadStates ->
            binding.swipeRefreshLayout.isRefreshing = combinedLoadStates.source.refresh is LoadState.Loading
            binding.errorTxt.isVisible = combinedLoadStates.source.refresh is LoadState.Error
        }
    }

    private fun initRv(){
        binding.swipeRefreshLayout.setColorSchemeColors(Color.WHITE)
        binding.swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.BLACK)

        binding.pagingRv.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = coinsPagingAdapter.withLoadStateFooter(FooterAdapter{
                coinsPagingAdapter.retry() })
        }

        binding.searchRv.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = searchAdapter
        }
    }

    private fun submitData(pagingData: PagingData<CoinItem>){
        lifecycleScope.launch{
            coinsPagingAdapter.submitData(pagingData)
        }
    }

    private suspend fun collectCoin(){
        viewModel.state.collectLatest { coinItem ->
            coinItem.coinListData?.let { submitData(it) }
        }
    }

}