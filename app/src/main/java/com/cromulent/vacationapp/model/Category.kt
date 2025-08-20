package com.cromulent.vacationapp.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Category(
    val key: String,
    val title: String): Parcelable