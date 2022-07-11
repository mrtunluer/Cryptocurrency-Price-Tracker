package com.mertdev.cryptocurrencypricetracker.data.repo

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class FirebaseRepo @Inject constructor(firebase: Firebase) {

    val firebaseAuth = firebase.auth
    val firebaseUser = firebaseAuth.currentUser
    private val firebaseFirestore = firebase.firestore

    fun signUpUser(email: String, password: String) =
        firebaseAuth.createUserWithEmailAndPassword(email,password)

    fun signInUser(email: String, password: String) =
        firebaseAuth.signInWithEmailAndPassword(email, password)

    fun addUserDb(uid: String, user: HashMap<String, Any>) =
        firebaseFirestore.collection("Users")
            .document(uid)
            .set(user)

}