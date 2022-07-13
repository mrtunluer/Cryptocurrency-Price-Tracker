package com.mertdev.cryptocurrencypricetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.mertdev.cryptocurrencypricetracker.data.repo.FirebaseRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseRepo: FirebaseRepo
) : ViewModel(){

    val isItLoggedIn = flow {
        emit(firebaseRepo.firebaseUser)
    }

    fun signUp(email: String, password: String) =
        firebaseRepo.signUpUser(email, password)

    fun signIn(email: String, password: String) =
        firebaseRepo.signInUser(email, password)

    fun addUserDb(uid: String, user: HashMap<String, Any>) =
        firebaseRepo.addUserDb(uid, user)

    fun signOut() =
        firebaseRepo.signOut()

}