package com.cromulent.vacationapp.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.Serializable

@SuppressLint("ParcelCreator")
@Serializable
data class Category(
    val key: String,
    val title: String): Parcelable {
    override fun describeContents(): Int {
        TODO("Not yet implemented")
        
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        TODO("Not yet implemented")
    }
}