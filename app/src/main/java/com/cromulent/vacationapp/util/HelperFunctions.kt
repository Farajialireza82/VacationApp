package com.cromulent.vacationapp.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
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

@Composable
fun rememberLocationPermissionHandler(
    onPermissionGranted: () -> Unit = {},
    onPermissionDenied: () -> Unit = {},
    onNeedSettings: () -> Unit = {}
): () -> Unit {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            val activity = context as? ComponentActivity
            val shouldShowRationale = activity?.let {
                ActivityCompat.shouldShowRequestPermissionRationale(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } ?: false

            if (!shouldShowRationale) {
                onNeedSettings()
            } else {
                onPermissionDenied()
            }
        }
    }

    return remember {
        {
            val permission = ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

            if (permission == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted()
            } else {
                launcher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }
    }
}

// Helper function to open app settings
fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}