package com.cromulent.vacationapp.presentation.detailsScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Accessible
import androidx.compose.material.icons.rounded.BusinessCenter
import androidx.compose.material.icons.rounded.Deck
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Kitchen
import androidx.compose.material.icons.rounded.LocalCafe
import androidx.compose.material.icons.rounded.LocalLaundryService
import androidx.compose.material.icons.rounded.LocalParking
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.Pool
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.Shower
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Thermostat
import androidx.compose.material.icons.rounded.Tv
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cromulent.vacationapp.R
import com.cromulent.vacationapp.ui.theme.VacationAppTheme
import com.cromulent.vacationapp.util.Samples.amenitiesList


@Composable
fun AmenitiesList(
    modifier: Modifier = Modifier,
    amenities: List<String>?,
    onSeeAllClicked: () -> Unit
) {
    val priorityAmenities = getPriorityAmenities(amenities)

    LazyRow(
        modifier = modifier,
    ) {
        items (priorityAmenities) { (amenity, icon) ->
            Spacer(Modifier.size(12.dp))
            AmenityBox(
                text = formatAmenityText(amenity),
                icon = icon
            )
        }

        // "See All" button
        item {

            Spacer(Modifier.size(12.dp))

            AmenityBox(
                text = "See All",
                icon = Icons.Rounded.MoreHoriz
            ) {
                onSeeAllClicked()
            }

            Spacer(Modifier.size(12.dp))

        }
    }
}


@Composable
fun AmenityBox(
    modifier: Modifier = Modifier,
    text: String?,
    icon: ImageVector?,
    onClick: () -> Unit = {}) {

    Column(
        modifier
            .height(74.dp)
            .width(77.dp)
            .background(
                color = colorResource(R.color.box_background),
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape((16.dp)))
            .clickable{
                onClick()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        icon?.let {
        Icon(
            modifier = Modifier
                .size(32.dp),
            imageVector = icon,
            tint = colorResource(R.color.subtitle),
            contentDescription = null
        )
            }

        text?.let {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                text = it,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                color = colorResource(R.color.subtitle)
            )

        }

    }

}

@Composable
fun getAmenityIcon(amenity: String): ImageVector? {
    val amenityLower = amenity.lowercase()

    return when {
        // Internet & WiFi
        amenityLower.contains("wifi") || amenityLower.contains("internet") -> Icons.Rounded.Wifi

        // Pool & Water
        amenityLower.contains("pool") -> Icons.Rounded.Pool
        amenityLower.contains("shower") || amenityLower.contains("bath") -> Icons.Rounded.Shower

        // Food & Dining
        amenityLower.contains("breakfast") || amenityLower.contains("dining") -> Icons.Rounded.Restaurant
        amenityLower.contains("coffee") || amenityLower.contains("tea") -> Icons.Rounded.LocalCafe
        amenityLower.contains("kitchen") || amenityLower.contains("microwave") -> Icons.Rounded.Kitchen

        // Comfort & Climate
        amenityLower.contains("air conditioning") || amenityLower.contains("heating") -> Icons.Rounded.Thermostat
        amenityLower.contains("fitness") || amenityLower.contains("gym") -> Icons.Rounded.FitnessCenter

        // Services
        amenityLower.contains("parking") -> Icons.Rounded.LocalParking
        amenityLower.contains("laundry") || amenityLower.contains("dry cleaning") -> Icons.Rounded.LocalLaundryService
        amenityLower.contains("pets") -> Icons.Rounded.Pets

        // Accessibility
        amenityLower.contains("wheelchair") || amenityLower.contains("accessible") ||
                amenityLower.contains("disabled") -> Icons.AutoMirrored.Rounded.Accessible

        // Entertainment
        amenityLower.contains("tv") -> Icons.Rounded.Tv
        amenityLower.contains("rooftop") || amenityLower.contains("terrace") -> Icons.Rounded.Deck

        // Business
        amenityLower.contains("meeting") || amenityLower.contains("conference") -> Icons.Rounded.BusinessCenter

        // Safety & Security
        amenityLower.contains("safe") -> Icons.Rounded.Security

        // Default
        else -> Icons.Rounded.Star // or null if you don't want to show unmapped amenities
    }
}


@Composable
fun getPriorityAmenities(amenities: List<String>?): List<Pair<String, ImageVector>> {
    // Priority levels with keyword groups that should be treated as one category
    val priorityMap = mapOf(
        1 to listOf("wifi", "internet"),
        2 to listOf("pool", "fitness", "gym"),
        3 to listOf("breakfast", "dining", "restaurant"),
        4 to listOf("parking"),
        5 to listOf("air conditioning", "heating", "thermostat"),
        6 to listOf("laundry", "dry cleaning"),
        7 to listOf("pets", "pet"),
        8 to listOf("accessible", "wheelchair", "disabled"),
        9 to listOf("kitchen", "microwave", "kitchenette"),
        10 to listOf("tv", "television"),
        11 to listOf("rooftop", "terrace", "deck"),
        12 to listOf("meeting", "conference", "business"),
        13 to listOf("safe", "security"),
        14 to listOf("shower", "bath")
    )

    val foundAmenities = mutableListOf<Pair<String, ImageVector>>()
    val usedCategories = mutableSetOf<Int>() // Track which priority categories we've already used

    // Go through priority levels
    for (priority in priorityMap.keys.sorted()) {
        if (usedCategories.contains(priority)) continue

        val keywords = priorityMap[priority] ?: continue

        // Find the BEST matching amenity for this category
        val bestMatch = findBestAmenityMatch(amenities, keywords)

        bestMatch?.let { amenity ->
            getAmenityIcon(amenity)?.let { icon ->
                foundAmenities.add(amenity to icon)
                usedCategories.add(priority) // Mark this category as used
                if (foundAmenities.size >= 4) return foundAmenities
            }
        }
    }

    return foundAmenities
}


// Helper function to find the best matching amenity from a group of keywords
fun findBestAmenityMatch(amenities: List<String>?, keywords: List<String>): String? {
    val matches = mutableListOf<String>()

    // Find all amenities that match any of the keywords
    for (keyword in keywords) {
        val found = amenities?.filter { amenity ->
            amenity.lowercase().contains(keyword)
        }
        found?.let {
        matches.addAll(it)
        }
    }

    if (matches.isEmpty()) return null

    // Return the "best" match based on preference order
    return when {
        // For wifi/internet, prefer more specific terms
        keywords.contains("wifi") || keywords.contains("internet") -> {
            matches.find { it.lowercase() == "wifi" }
                ?: matches.find { it.lowercase() == "free wifi" }
                ?: matches.find { it.lowercase().contains("wifi") }
                ?: matches.find { it.lowercase().contains("internet") }
                ?: matches.first()
        }

        // For pools, prefer more specific terms
        keywords.contains("pool") -> {
            matches.find { it.lowercase().contains("indoor pool") }
                ?: matches.find { it.lowercase().contains("heated pool") }
                ?: matches.find { it.lowercase().contains("pool") }
                ?: matches.first()
        }

        // For breakfast, prefer more comprehensive options
        keywords.contains("breakfast") -> {
            matches.find { it.lowercase().contains("breakfast included") }
                ?: matches.find { it.lowercase().contains("breakfast buffet") }
                ?: matches.find { it.lowercase().contains("breakfast") }
                ?: matches.first()
        }

        // For fitness, prefer more specific terms
        keywords.contains("fitness") -> {
            matches.find { it.lowercase().contains("fitness center") }
                ?: matches.find { it.lowercase().contains("gym") }
                ?: matches.find { it.lowercase().contains("fitness") }
                ?: matches.first()
        }

        // Default: return the shortest match (usually more concise)
        else -> matches.minByOrNull { it.length } ?: matches.first()
    }
}

fun formatAmenityText(amenity: String): String {
    val amenityLower = amenity.lowercase()

    return when {
        // Shortened versions for common long names
        amenityLower.contains("air conditioning") -> "A/C"
        amenityLower.contains("fitness center") -> "Fitness"
        amenityLower.contains("free wifi") || amenityLower == "wifi" -> "WiFi"
        amenityLower.contains("breakfast") -> "Breakfast"
        amenityLower.contains("swimming pool") || amenityLower.contains("indoor pool") -> "Pool"
        amenityLower.contains("parking") -> "Parking"
        amenityLower.contains("laundry") -> "Laundry"
        amenityLower.contains("pets allowed") -> "Pet Friendly"
        amenityLower.contains("wheelchair") || amenityLower.contains("accessible") -> "Accessible"

        // If the name is too long, truncate it
        amenity.length > 10 -> {
            val words = amenity.split(" ")
            if (words.size > 1) words[0] else amenity.take(8) + "..."
        }

        else -> amenity
    }
}

@Preview
@Composable
private fun AmenityBoxPrev() {

    VacationAppTheme {
        AmenitiesList(amenities = amenitiesList){}
    }

}