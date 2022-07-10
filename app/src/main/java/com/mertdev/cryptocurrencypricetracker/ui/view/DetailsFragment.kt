package com.mertdev.cryptocurrencypricetracker.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.mertdev.cryptocurrencypricetracker.R
import com.mertdev.cryptocurrencypricetracker.databinding.FragmentDetailsBinding
import com.mertdev.cryptocurrencypricetracker.databinding.FragmentHomeBinding
import com.mertdev.cryptocurrencypricetracker.ui.viewmodel.DetailsFragmentViewModel
import com.mertdev.cryptocurrencypricetracker.ui.viewmodel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: DetailsFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)
    }

}