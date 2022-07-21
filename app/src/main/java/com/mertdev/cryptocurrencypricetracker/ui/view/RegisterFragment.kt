package com.mertdev.cryptocurrencypricetracker.ui.view

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FieldValue
import com.mertdev.cryptocurrencypricetracker.R
import com.mertdev.cryptocurrencypricetracker.databinding.FragmentRegisterBinding
import com.mertdev.cryptocurrencypricetracker.ui.viewmodel.AuthViewModel
import com.mertdev.cryptocurrencypricetracker.utils.extensions.initDialog
import com.mertdev.cryptocurrencypricetracker.utils.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var progress: Dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
        initDialog()

        viewLifecycleOwner.lifecycleScope.launch {
            collectLoginState()
        }

        binding.signInTxt.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.registerCard.setOnClickListener {
            if (!isExistBlankText()){
                progress.show()
                signUp(binding.emailTxt.text.toString(), binding.passwordTxt.text.toString())
                    .addOnSuccessListener { authResult ->
                        authResult.user?.let { addUserDb(it.uid) }
                    }.addOnFailureListener {
                        progress.dismiss()
                        requireContext().showToast(it.message.toString())
                    }
            }else{
                requireContext().showToast("please fill in all the blanks")
            }
        }

    }

    private fun addUserDb(uid: String) {

        val user = hashMapOf(
            "uid" to uid,
            "email" to binding.emailTxt.text.toString(),
            "username" to binding.userNameTxt.text.toString(),
            "created_at" to FieldValue.serverTimestamp()
        )

        viewModel.addUserDb(uid, user)
            .addOnSuccessListener {
                progress.dismiss()
                findNavController().navigate(R.id.action_registerFragment_to_viewPagerFragment)
            }.addOnFailureListener {
                progress.dismiss()
                requireContext().showToast(it.message.toString())
            }

    }

    private fun signUp(email: String, password: String) =
        viewModel.signUp(email, password)

    private fun isExistBlankText(): Boolean {
        if (binding.userNameTxt.text.isNullOrBlank()
            || binding.emailTxt.text.isNullOrBlank()
            || binding.passwordTxt.text.isNullOrBlank()){
            return true
        }
        return false
    }

    private suspend fun collectLoginState(){
        viewModel.isItLoggedIn.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect { user ->
            if (user != null){
                findNavController().navigate(R.id.action_registerFragment_to_viewPagerFragment)
            }
        }
    }

    private fun initDialog(){
        progress = Dialog(requireContext())
        progress.initDialog(R.layout.custom_progress)
    }

}