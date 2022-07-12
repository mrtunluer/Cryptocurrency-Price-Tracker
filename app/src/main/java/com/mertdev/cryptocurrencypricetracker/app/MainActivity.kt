package com.mertdev.cryptocurrencypricetracker.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.ui.setupWithNavController
import com.mertdev.cryptocurrencypricetracker.R
import com.mertdev.cryptocurrencypricetracker.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.findNavController
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.mertdev.cryptocurrencypricetracker.ui.view.DetailsFragment
import com.mertdev.cryptocurrencypricetracker.ui.view.LoginFragment
import com.mertdev.cryptocurrencypricetracker.ui.view.RegisterFragment


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController( R.id.fragmentContainer)
        binding.bottomNavigationView.setupWithNavController(navController)

        bottomNavVisibility()

    }

    private fun bottomNavVisibility(){
        supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
                TransitionManager.beginDelayedTransition(binding.root, Slide(Gravity.BOTTOM).excludeTarget(R.id.fragmentContainer, true))
                when (f) {
                    is RegisterFragment -> hideBottomNav()
                    is LoginFragment -> hideBottomNav()
                    is DetailsFragment -> hideBottomNav()
                    else -> showBottomNav()
                }
            }
        }, true)
    }


    private fun showBottomNav() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.bottomNavigationView.visibility = View.GONE
    }

}