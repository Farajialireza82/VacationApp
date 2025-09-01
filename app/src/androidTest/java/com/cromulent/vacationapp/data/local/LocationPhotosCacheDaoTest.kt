package com.cromulent.vacationapp.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.cromulent.vacationapp.data.model.LocationPhotoListEntity
import com.cromulent.vacationapp.di.ManagerModule
import com.cromulent.vacationapp.di.NetworkModule
import com.cromulent.vacationapp.di.RepositoryModule
import com.cromulent.vacationapp.model.ImageData
import com.cromulent.vacationapp.model.Images
import com.cromulent.vacationapp.model.LocationPhoto
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.collections.mutableListOf

@HiltAndroidTest
@SmallTest
@UninstallModules(
    value = [
        RepositoryModule::class,
        ManagerModule::class,
        NetworkModule::class
    ]
)
class LocationPhotosCacheDaoTest {



    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var locationDatabase: LocationDatabase
    private lateinit var locationPhotoCacheDao: LocationPhotosCacheDao

    @Before
    fun setup(){
        hiltRule.inject()
        locationPhotoCacheDao = locationDatabase.locationPhotosCacheDao
    }

    @After
    fun tearDown(){
        locationDatabase.close()
    }

    @Test
    fun doesLocationPhotosGetCached_returnsTrue() = runTest{

        val locationPhotoList = mutableListOf<LocationPhoto>()

        for(i in 1..10) {
            locationPhotoList.add(
                LocationPhoto(
                    id = i.toLong(),
                    caption = "Beautiful sunset over Golden Gate Bridge",
                    images = Images(
                        thumbnail = ImageData(
                            height = 150,
                            width = 150,
                            url = "https://example.com/photos/12345/thumbnail.jpg"
                        ),
                        small = ImageData(
                            height = 300,
                            width = 400,
                            url = "https://example.com/photos/12345/small.jpg"
                        ),
                        medium = ImageData(
                            height = 600,
                            width = 800,
                            url = "https://example.com/photos/12345/medium.jpg"
                        ),
                        large = ImageData(
                            height = 1200,
                            width = 1600,
                            url = "https://example.com/photos/12345/large.jpg"
                        ),
                        original = ImageData(
                            height = 2400,
                            width = 3200,
                            url = "https://example.com/photos/12345/original.jpg"
                        )
                    )
                )
            )
        }

        locationPhotoCacheDao.cacheLocationPhotos(LocationPhotoListEntity("1", locationPhotoList))

        assertThat(locationPhotoCacheDao.getCachedLocationPhotos("1") != null).isTrue()

    }

    @Test
    fun whenNoPhotosCached_returnNull() = runTest {

        assertThat(locationPhotoCacheDao.getCachedLocationPhotos("123AD") == null).isTrue()

    }

}