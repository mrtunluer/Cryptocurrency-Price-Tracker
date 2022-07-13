package com.mertdev.cryptocurrencypricetracker.data.repo

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mertdev.cryptocurrencypricetracker.data.model.CoinItem
import javax.inject.Inject

class FirebaseRepo @Inject constructor(firebase: Firebase) {

    val firebaseAuth = firebase.auth
    val firebaseUser = firebaseAuth.currentUser
    private val firebaseFirestore = firebase.firestore

    fun signOut() =
        firebaseAuth.signOut()

    fun signUpUser(email: String, password: String) =
        firebaseAuth.createUserWithEmailAndPassword(email,password)

    fun signInUser(email: String, password: String) =
        firebaseAuth.signInWithEmailAndPassword(email, password)

    fun addUserDb(uid: String, user: HashMap<String, Any>) =
        firebaseFirestore.collection("Users")
            .document(uid)
            .set(user)

    fun getFavorite(coinId: String) =
        firebaseUser?.let {
            firebaseFirestore.collection("Users")
                .document(it.uid)
                .collection("Favorite")
                .document(coinId)
                .get()
        }

    fun addFavorite(coinId: String, coinItem: CoinItem) =
        firebaseUser?.let {
            firebaseFirestore.collection("Users")
                .document(it.uid)
                .collection("Favorite")
                .document(coinId)
                .set(coinItem)
        }

    fun deleteFavorite(coinId: String) =
        firebaseUser?.let {
            firebaseFirestore.collection("Users")
                .document(it.uid)
                .collection("Favorite")
                .document(coinId)
                .delete()
        }

}