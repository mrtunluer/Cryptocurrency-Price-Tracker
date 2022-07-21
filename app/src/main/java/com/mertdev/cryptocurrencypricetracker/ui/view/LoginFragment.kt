package com.mertdev.cryptocurrencypricetracker.ui.view

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mertdev.cryptocurrencypricetracker.R
import com.mertdev.cryptocurrencypricetracker.databinding.FragmentLoginBinding
import com.mertdev.cryptocurrencypricetracker.ui.viewmodel.AuthViewModel
import com.mertdev.cryptocurrencypricetracker.utils.extensions.initDialog
import com.mertdev.cryptocurrencypricetracker.utils.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var progress: Dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        initDialog()

        binding.loginCard.setOnClickListener {
            if (!isExistBlankText()){
                progress.show()
                signIn(binding.emailTxt.text.toString(), binding.passwordTxt.text.toString())
                    .addOnSuccessListener {
                        onSuccessLogin()
                    }.addOnFailureListener {
                        onFailureLogin(it)
                    }
            }else {
                requireContext().showToast("please fill in all the blanks")
            }
        }

        binding.signUpTxt.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun onSuccessLogin(){
        progress.dismiss()
        findNavController().navigate(R.id.action_loginFragment_to_viewPagerFragment)
    }

    private fun onFailureLogin(e: Exception){
        progress.dismiss()
        requireContext().showToast(e.message.toString())
    }

    private fun initDialog(){
        progress = Dialog(requireContext())
        progress.initDialog(R.layout.custom_progress)
    }

    private fun signIn(email: String, password: String) =
        viewModel.signIn(email, password)

    private fun isExistBlankText(): Boolean {
        if (binding.emailTxt.text.isNullOrBlank()
            || binding.passwordTxt.text.isNullOrBlank()){
            return true
        }
        return false
    }

}