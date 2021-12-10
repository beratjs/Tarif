package com.android.tarif

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.gorselIndir(url: ByteArray?) {
    Glide.with(context).load(url).into(this)
}