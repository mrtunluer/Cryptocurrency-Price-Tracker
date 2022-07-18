package com.mertdev.cryptocurrencypricetracker.data.repo

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mertdev.cryptocurrencypricetracker.data.model.CoinItem
import com.mertdev.cryptocurrencypricetracker.utils.DataStatus
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirebaseRepo @Inject constructor(firebase: Firebase) {

    private val firebaseAuth = firebase.auth
    val firebaseUser = firebaseAuth.currentUser
    private val firebaseFirestore = firebase.firestore
    private val favoriteCoinsQuery = firebaseUser?.let {
        firebaseFirestore.collection("Users")
            .document(it.uid)
            .collection("Favorites")
            .orderBy("date", Query.Direction.DESCENDING)
    }

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

    fun isFavorite(coinId: String) =
        firebaseUser?.let {
            firebaseFirestore.collection("Users")
                .document(it.uid)
                .collection("Favorites")
                .document(coinId)
                .get()
        }

    fun addFavorite(coinId: String, coinItem: CoinItem) =
        firebaseUser?.let {
            firebaseFirestore.collection("Users")
                .document(it.uid)
                .collection("Favorites")
                .document(coinId)
                .set(coinItem)
        }

    fun deleteFavorite(coinId: String) =
        firebaseUser?.let {
            firebaseFirestore.collection("Users")
                .document(it.uid)
                .collection("Favorites")
                .document(coinId)
                .delete()
        }

    fun getFavorites(): Flow<DataStatus<List<CoinItem>>> = callbackFlow {
        trySend(DataStatus.Loading()).isSuccess
        val listener = favoriteCoinsQuery?.addSnapshotListener { snapshots, exception ->
            exception?.let {
                trySend(DataStatus.Error(it.message.toString())).isSuccess
                cancel()
            }
            snapshots?.let { querySnapshot ->
                if (querySnapshot.isEmpty)
                    trySend(DataStatus.Empty()).isSuccess
                else
                    trySend(DataStatus.Success(querySnapshot.toObjects(CoinItem::class.java))).isSuccess
            } ?: trySend(DataStatus.Empty()).isSuccess
        }
        awaitClose { listener?.remove() }
    }

}