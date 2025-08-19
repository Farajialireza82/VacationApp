package com.cromulent.vacationapp.util

import com.cromulent.vacationapp.model.Address
import com.cromulent.vacationapp.model.Location

object Samples {

    val location = Location(
        locationId = "258730",
        name = "Beacon Townhouse Inn 1023",
        distance = "3.2",
        description = "",
        reviewCount = "3221",
        rating = "4.3",
        latitude = "-32.12345",
        longitude = "-32.12345",
        amenities = listOf("wifi", "Pets Allowed", "Dry Cleaning", "Non-smoking rooms"),
        seeAllPhotosLink = "https://www.tripadvisor.com/Hotel_Review-g60745-d4325990-m66827-Reviews-Residence_Inn_Boston_Back_Bay_Fenway-Boston_Massachusetts.html#photos",
        priceLevel = "$$$$",
        locationPhotos = null,
    )

    val locationsList = listOf(
        Location(
            locationId = "189745",
            name = "Golden Gate Bridge Vista Point",
            distance = "2.3",
        ),
        Location(
            locationId = "456123",
            name = "Central Park Zoo",
            distance = "1.1"
        ),
        Location(
            locationId = "789012",
            name = "Pike Place Market",
            distance = "0.5"
        ),
        Location(
            locationId = "334567",
            name = "Navy Pier",
            distance = "3.2"
        ),
        Location(
            locationId = "901234",
            name = "South Beach",
            distance = "5.7"
        ),
        Location(
            locationId = "567890",
            name = "French Quarter",
            distance = "0.8"
        ),
        Location(
            locationId = "123789",
            name = "Red Rocks Amphitheatre",
            distance = "15.4"
        ),
        Location(
            locationId = "445566",
            name = "Hollywood Walk of Fame",
            distance = "4.1"
        ),
        Location(
            locationId = "778899",
            name = "Austin City Limits Music Festival",
            distance = "7.9"
        ),
        Location(
            locationId = "112233",
            name = "Freedom Trail",
            distance = "1.6"
        )
    )


}