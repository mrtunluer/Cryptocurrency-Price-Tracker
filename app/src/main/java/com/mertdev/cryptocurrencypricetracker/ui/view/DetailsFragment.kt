package com.mertdev.cryptocurrencypricetracker.ui.view

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.RadioGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.mertdev.cryptocurrencypricetracker.R
import com.mertdev.cryptocurrencypricetracker.data.model.CoinDetails
import com.mertdev.cryptocurrencypricetracker.data.model.CoinItem
import com.mertdev.cryptocurrencypricetracker.databinding.FragmentDetailsBinding
import com.mertdev.cryptocurrencypricetracker.ui.viewmodel.DetailsFragmentViewModel
import com.mertdev.cryptocurrencypricetracker.utils.Constants.INITIAL_INTERVAL
import com.mertdev.cryptocurrencypricetracker.utils.DataStatus
import com.mertdev.cryptocurrencypricetracker.utils.initDialog
import com.mertdev.cryptocurrencypricetracker.utils.loadImageFromUrl
import com.mertdev.cryptocurrencypricetracker.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: DetailsFragmentViewModel by viewModels()
    private var isFavorite: Boolean? = null
    private var coinItem: CoinItem? = null

    private var handler: Handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private var delay: Long = INITIAL_INTERVAL.toLong()

    private lateinit var refreshIntervalDialog: Dialog
    private lateinit var intervalRadioGroup: RadioGroup

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)

        init()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    collectFavoriteData()
                }
                launch {
                    collectDetailsData()
                }
            }
        }

        observeDataStore()

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getFavorite()
            viewModel.getCoinDetails()
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.saveImg.setOnClickListener {
            isFavorite?.let { favoriteItBefore ->
                if (favoriteItBefore)
                    deleteFavorite()
                else
                    addFavorite()
            }
        }

        binding.timeLineImg.setOnClickListener {
            refreshIntervalDialog.show()
        }

        intervalRadioGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.fiveSec -> saveDataStore("5")
                R.id.fifteenSec -> saveDataStore("15")
                R.id.thirtySec -> saveDataStore("30")
                R.id.fortyFiveSec -> saveDataStore("45")
            }
        }

    }

    private fun convertToMillis(interval: String) =
        interval.toLong() * 1000

    private fun observeDataStore(){
        viewModel.readFromDataStore.observe(viewLifecycleOwner) { interval ->
            interval?.let {
                when (it) {
                    "5" -> intervalRadioGroup.check(R.id.fiveSec)
                    "15" -> intervalRadioGroup.check(R.id.fifteenSec)
                    "30" -> intervalRadioGroup.check(R.id.thirtySec)
                    "45" -> intervalRadioGroup.check(R.id.fortyFiveSec)
                }
                delay = convertToMillis(it)
                autoRefreshDetailsData(delay)
            }
        }
    }

    private fun saveDataStore(interval: String){
        runnable?.let { handler.removeCallbacks(it) }
        viewModel.saveToDataStore(interval)
    }

    private fun autoRefreshDetailsData(delay: Long){
        handler.postDelayed(Runnable {
            viewModel.getCoinDetails()
            handler.postDelayed(runnable!!, delay)
        }.also { runnable = it }, delay)
    }

    private fun deleteFavorite(){
        viewModel.deleteFavorite()?.addOnSuccessListener {
            binding.saveImg.setImageResource(R.drawable.ic_baseline_thumb_up_alt_24)
            isFavorite = false
        }
    }

    private fun addFavorite(){
        coinItem?.let {
            viewModel.addFavorite(it)?.addOnSuccessListener {
                binding.saveImg.setImageResource(R.drawable.ic_baseline_thumb_up_alt_like_24)
                isFavorite = true
            }
        }
    }

    private suspend fun collectFavoriteData(){
        viewModel.favoriteState.collect{ uiState ->
            when (uiState){is DataStatus.Loading -> loadingForFavoriteData()
                is DataStatus.Empty -> emptyForFavoriteData()
                is DataStatus.Error -> errorForFavoriteData()
                is DataStatus.Success -> successForFavoriteData()
            }
        }
    }

    private suspend fun collectDetailsData(){
        viewModel.detailState.collect{ uiState ->
            when (uiState){
                is DataStatus.Loading -> binding.swipeRefreshLayout.isRefreshing = true
                is DataStatus.Empty -> emptyForDetailsData()
                is DataStatus.Error -> errorForDetailsData()
                is DataStatus.Success -> uiState.data?.let {
                    successForDetailsData(it)
                    coinItem = CoinItem(
                        it.id,
                        it.marketData?.currentPrice?.usd,
                        it.name,
                        it.symbol,
                        it.image?.large
                    )
                }
            }
        }
    }

    private fun successForDetailsData(details: CoinDetails){
        binding.swipeRefreshLayout.isRefreshing = false
        binding.titleTxt.text = details.name.plus(" "+details.symbol?.uppercase())
        details.hashingAlgorithm?.let { binding.hashingAlg.text = it }

        binding.describeTxt.text = HtmlCompat.fromHtml(details.description?.en.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.describeTxt.movementMethod = LinkMovementMethod.getInstance()
        binding.describeTxt.setLinkTextColor(Color.CYAN)

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

    private fun loadingForFavoriteData(){
        binding.swipeRefreshLayout.isRefreshing = true
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun emptyForFavoriteData(){
        binding.progressBar.visibility = View.GONE
        binding.swipeRefreshLayout.isRefreshing = false
        binding.saveImg.setImageResource(R.drawable.ic_baseline_thumb_up_alt_24)
        isFavorite = false
    }

    private fun errorForFavoriteData(){
        binding.progressBar.visibility = View.GONE
        binding.swipeRefreshLayout.isRefreshing = false
        requireContext().showToast("Failed to fetch data")
    }

    private fun successForFavoriteData(){
        binding.progressBar.visibility = View.GONE
        binding.swipeRefreshLayout.isRefreshing = false
        binding.saveImg.setImageResource(R.drawable.ic_baseline_thumb_up_alt_like_24)
        isFavorite = true
    }

    private fun init(){
        binding.swipeRefreshLayout.setColorSchemeColors(Color.WHITE)
        binding.swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.BLACK)

        refreshIntervalDialog = Dialog(requireContext())
        refreshIntervalDialog.initDialog(R.layout.refresh_interval_dialog, true)
        intervalRadioGroup = refreshIntervalDialog.findViewById(R.id.intervalRadioGroup)
    }

}