package com.mertdev.cryptocurrencypricetracker.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.mertdev.cryptocurrencypricetracker.R
import com.mertdev.cryptocurrencypricetracker.databinding.FragmentFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private lateinit var binding: FragmentFavoriteBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteBinding.bind(view)

    }
}