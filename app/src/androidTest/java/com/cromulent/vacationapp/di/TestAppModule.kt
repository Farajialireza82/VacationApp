package com.cromulent.vacationapp.di

import android.app.Application
import androidx.room.Room
import com.cromulent.vacationapp.data.local.LocationCacheDao
import com.cromulent.vacationapp.data.local.LocationDatabase
import com.cromulent.vacationapp.data.local.LocationPhotosCacheDao
import com.cromulent.vacationapp.data.local.LocationTypeConvertor
import com.cromulent.vacationapp.data.remote.OpenWeatherMapApi
import com.cromulent.vacationapp.data.remote.VacationApi
import com.cromulent.vacationapp.domain.manager.GpsRepository
import com.cromulent.vacationapp.domain.manager.LocalUserManager
import com.cromulent.vacationapp.domain.repository.OpenWeatherMapRepository
import com.cromulent.vacationapp.domain.repository.VacationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk
import javax.inject.Singleton
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class, RepositoryModule::class, NetworkModule::class, DatabaseModule::class]
)
object TestAppModule {

    // From AppModule
    @Provides
    @Singleton
    fun provideLocalUserManager(): LocalUserManager {
        return mockk(relaxed = true)
    }

    // From NetworkModule - Mock the APIs
    @Provides
    @Singleton
    fun provideVacationApi(): VacationApi {
        return mockk(relaxed = true)
    }

    @Provides
    @Singleton
    fun provideOpenWeatherMapApi(): OpenWeatherMapApi {
        return mockk(relaxed = true)
    }

    // From DatabaseModule - Use in-memory database for testing
    @Provides
    @Singleton
    fun provideLocationDatabase(
        application: Application
    ): LocationDatabase {
        return Room.inMemoryDatabaseBuilder(
            context = application,
            klass = LocationDatabase::class.java,
        ).addTypeConverter(LocationTypeConvertor())
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    @Singleton
    fun provideLocationCacheDao(
        newsDatabase: LocationDatabase
    ): LocationCacheDao = newsDatabase.locationCacheDao

    @Provides
    @Singleton
    fun provideLocationPhotosCacheDao(
        newsDatabase: LocationDatabase
    ): LocationPhotosCacheDao = newsDatabase.locationPhotosCacheDao

    // From RepositoryModule - Mock all repositories
    @Provides
    @Singleton
    fun provideVacationRepository(): VacationRepository {
        return mockk(relaxed = true)
    }

    @Provides
    @Singleton
    fun provideGpsRepository(): GpsRepository {
        return mockk(relaxed = true)
    }

    @Provides
    @Singleton
    fun provideOpenWeatherMapRepository(): OpenWeatherMapRepository {
        return mockk(relaxed = true)
    }
}