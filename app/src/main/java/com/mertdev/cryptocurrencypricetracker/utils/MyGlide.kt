package com.mertdev.cryptocurrencypricetracker.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImageFromUrl(url: String) {
    Glide.with(this)
        .load(url)
        .into(this)
}