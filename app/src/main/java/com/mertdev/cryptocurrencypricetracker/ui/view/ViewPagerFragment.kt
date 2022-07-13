package com.mertdev.cryptocurrencypricetracker.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.mertdev.cryptocurrencypricetracker.R
import com.mertdev.cryptocurrencypricetracker.adapter.ViewPagerAdapter
import com.mertdev.cryptocurrencypricetracker.databinding.FragmentFavoriteBinding
import com.mertdev.cryptocurrencypricetracker.databinding.FragmentViewPagerBinding
import com.mertdev.cryptocurrencypricetracker.utils.Constants.TABS_FRAGMENT_TEXT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewPagerFragment : Fragment(R.layout.fragment_view_pager) {
    private lateinit var binding: FragmentViewPagerBinding
    private lateinit var adapter: ViewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentViewPagerBinding.bind(view)

        adapter = ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = TABS_FRAGMENT_TEXT[position]
        }.attach()

    }

}