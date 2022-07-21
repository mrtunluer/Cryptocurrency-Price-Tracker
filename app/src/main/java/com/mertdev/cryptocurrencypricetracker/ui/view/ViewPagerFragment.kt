package com.mertdev.cryptocurrencypricetracker.ui.view

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayoutMediator
import com.mertdev.cryptocurrencypricetracker.R
import com.mertdev.cryptocurrencypricetracker.adapter.ViewPagerAdapter
import com.mertdev.cryptocurrencypricetracker.databinding.FragmentViewPagerBinding
import com.mertdev.cryptocurrencypricetracker.ui.viewmodel.AuthViewModel
import com.mertdev.cryptocurrencypricetracker.utils.Constants.TABS_FRAGMENT_TEXT
import com.mertdev.cryptocurrencypricetracker.utils.extensions.initDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewPagerFragment : Fragment(R.layout.fragment_view_pager) {

    private lateinit var binding: FragmentViewPagerBinding
    private lateinit var adapter: ViewPagerAdapter
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var logOutDialog: Dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentViewPagerBinding.bind(view)
        initDialog()

        adapter = ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = TABS_FRAGMENT_TEXT[position]
        }.attach()

        binding.logOutBtn.setOnClickListener {
            val yesBtn = logOutDialog.findViewById<MaterialCardView>(R.id.yesBtn)
            val noBtn = logOutDialog.findViewById<MaterialCardView>(R.id.noBtn)
            logOutDialog.show()

            yesBtn.setOnClickListener {
                signOut()
            }

            noBtn.setOnClickListener {
                logOutDialog.dismiss()
            }

        }

    }

    private fun signOut(){
        viewModel.signOut()
        findNavController().navigate(R.id.action_viewPagerFragment_to_registerFragment)
        logOutDialog.dismiss()
    }

    private fun initDialog(){
        logOutDialog = Dialog(requireContext())
        logOutDialog.initDialog(R.layout.log_out_dialog, true)
    }

}