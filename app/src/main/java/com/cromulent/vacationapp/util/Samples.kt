package com.cromulent.vacationapp.util

import com.cromulent.vacationapp.model.Address
import com.cromulent.vacationapp.model.Location

object Samples {

    val address = Address(
        street1 = "1023 Beacon St",
        city = "Brookline",
        state = "Massachusetts",
        country = "United States",
        postalcode = "02446-5609",
        addressString = "1023 Beacon St, Brookline, MA 02446-5609"
    )

    val location = Location(
        locationId = "258730",
        name = "Beacon Townhouse Inn 1023",
        distance = "0.0",
        bearing = "north",
        addressObj = address
    )

    val locationsList = listOf(
        Location(
            locationId = "189745",
            name = "Golden Gate Bridge Vista Point",
            distance = "2.3",
            bearing = "northwest",
            addressObj = Address(
                street1 = "Golden Gate Bridge",
                city = "San Francisco",
                state = "California",
                country = "United States",
                postalcode = "94129",
                addressString = "Golden Gate Bridge, San Francisco, CA 94129"
            )
        ),
        Location(
            locationId = "456123",
            name = "Central Park Zoo",
            distance = "1.1",
            bearing = "northeast",
            addressObj = Address(
                street1 = "64th Street & 5th Avenue",
                city = "New York",
                state = "New York",
                country = "United States",
                postalcode = "10021",
                addressString = "64th Street & 5th Avenue, New York, NY 10021"
            )
        ),
        Location(
            locationId = "789012",
            name = "Pike Place Market",
            distance = "0.5",
            bearing = "south",
            addressObj = Address(
                street1 = "85 Pike St",
                city = "Seattle",
                state = "Washington",
                country = "United States",
                postalcode = "98101",
                addressString = "85 Pike St, Seattle, WA 98101"
            )
        ),
        Location(
            locationId = "334567",
            name = "Navy Pier",
            distance = "3.2",
            bearing = "east",
            addressObj = Address(
                street1 = "600 E Grand Ave",
                city = "Chicago",
                state = "Illinois",
                country = "United States",
                postalcode = "60611",
                addressString = "600 E Grand Ave, Chicago, IL 60611"
            )
        ),
        Location(
            locationId = "901234",
            name = "South Beach",
            distance = "5.7",
            bearing = "southeast",
            addressObj = Address(
                street1 = "Ocean Drive",
                city = "Miami Beach",
                state = "Florida",
                country = "United States",
                postalcode = "33139",
                addressString = "Ocean Drive, Miami Beach, FL 33139"
            )
        ),
        Location(
            locationId = "567890",
            name = "French Quarter",
            distance = "0.8",
            bearing = "southwest",
            addressObj = Address(
                street1 = "Bourbon Street",
                city = "New Orleans",
                state = "Louisiana",
                country = "United States",
                postalcode = "70116",
                addressString = "Bourbon Street, New Orleans, LA 70116"
            )
        ),
        Location(
            locationId = "123789",
            name = "Red Rocks Amphitheatre",
            distance = "15.4",
            bearing = "west",
            addressObj = Address(
                street1 = "18300 W Alameda Pkwy",
                city = "Morrison",
                state = "Colorado",
                country = "United States",
                postalcode = "80465",
                addressString = "18300 W Alameda Pkwy, Morrison, CO 80465"
            )
        ),
        Location(
            locationId = "445566",
            name = "Hollywood Walk of Fame",
            distance = "4.1",
            bearing = "north",
            addressObj = Address(
                street1 = "Hollywood Boulevard",
                city = "Hollywood",
                state = "California",
                country = "United States",
                postalcode = "90028",
                addressString = "Hollywood Boulevard, Hollywood, CA 90028"
            )
        ),
        Location(
            locationId = "778899",
            name = "Austin City Limits Music Festival",
            distance = "7.9",
            bearing = "northwest",
            addressObj = Address(
                street1 = "2100 Barton Springs Rd",
                city = "Austin",
                state = "Texas",
                country = "United States",
                postalcode = "78746",
                addressString = "2100 Barton Springs Rd, Austin, TX 78746"
            )
        ),
        Location(
            locationId = "112233",
            name = "Freedom Trail",
            distance = "1.6",
            bearing = "northeast",
            addressObj = Address(
                street1 = "Boston Common",
                city = "Boston",
                state = "Massachusetts",
                country = "United States",
                postalcode = "02108",
                addressString = "Boston Common, Boston, MA 02108"
            )
        )
    )


}