package com.cromulent.vacationapp.util

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri


fun openMapWithLocation(
    context: Context,
    latitude: String?,
    longitude: String?,
    locationName: String?
) {
    latitude ?: return
    longitude ?: return

    // This will show the location name in the map pin
    val uri = "geo:$latitude,$longitude?q=$latitude,$longitude($locationName)".toUri()
    val intent = Intent(Intent.ACTION_VIEW, uri)

    context.startActivity(intent)
}

fun openWebsite(
    context: Context,
    url: String?
) {
    url ?: return

    // This will show the location name in the map pin
    val uri = url.toUri()
    val intent = Intent(Intent.ACTION_VIEW, uri)

    context.startActivity(intent)
}