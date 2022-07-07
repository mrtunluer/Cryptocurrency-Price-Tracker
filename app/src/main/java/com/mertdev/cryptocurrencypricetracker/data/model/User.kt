package com.mertdev.cryptocurrencypricetracker.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.io.Serializable

data class User (
    val uid: String = "",
    val email: String = "",
    val username: String = "",
    @ServerTimestamp
    val created_at: Timestamp? = null
): Serializable