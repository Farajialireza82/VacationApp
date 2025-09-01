package com.cromulent.vacationapp.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.cromulent.vacationapp.di.NetworkModule
import com.cromulent.vacationapp.di.ManagerModule
import com.cromulent.vacationapp.di.RepositoryModule
import com.cromulent.vacationapp.model.Address
import com.cromulent.vacationapp.model.Location
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@SmallTest
@UninstallModules(
    value = [
        RepositoryModule::class,
        ManagerModule::class,
        NetworkModule::class
    ]
)
class LocationCacheDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var locationDatabase: LocationDatabase
    private lateinit var locationCacheDao: LocationCacheDao


    @Before
    fun setup() {
        hiltRule.inject()
        locationCacheDao = locationDatabase.locationCacheDao

    }

    @After
    fun tearDown() {
        locationDatabase.close()
    }

    @Test
    fun doesLocationGetCached_returnsTrue() = runTest {


        val location = Location(
            locationId = "258730",
            name = "Aspen Mountain",
            distance = "3.2",
            description = "Completed in 1889, this colossal landmark, although initially hated by many Parisians, is now a famous symbol of French civic pride.",
            reviewCount = "3221",
            rating = "4.3",
            latitude = "-32.12345",
            longitude = "-32.12345",
            seeAllPhotosLink = "https://www.tripadvisor.com/Hotel_Review-g60745-d4325990-m66827-Reviews-Residence_Inn_Boston_Back_Bay_Fenway-Boston_Massachusetts.html#photos",
            priceLevel = "$$$$",
            locationPhotos = null,
            amenities = listOf(
                "Pets Allowed",
                "Free Internet"
            ),
            addressObject = Address(
                street1 = "Birch St",
                city = "San Francisco",
                state = "California",
                country = "US",
                postalcode = "14502",
                addressString = ""
            )
        )

        locationCacheDao.cacheLocation(location)

        val cachedLocation = (locationCacheDao.getCachedLocation(location.locationId))
        assertThat(cachedLocation != null).isTrue()

    }

    @Test
    fun returnsNull_whenLocationNotCached() = runTest {
        val cachedLocation = (locationCacheDao.getCachedLocation("12345"))
        assertThat(cachedLocation).isNull()

    }


}