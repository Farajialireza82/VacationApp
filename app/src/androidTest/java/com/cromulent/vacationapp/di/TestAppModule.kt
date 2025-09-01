package com.cromulent.vacationapp.di

import android.app.Application
import androidx.room.Room
import com.cromulent.vacationapp.data.local.LocationDatabase
import com.cromulent.vacationapp.data.local.LocationTypeConvertor
import com.cromulent.vacationapp.domain.manager.GpsRepository
import com.cromulent.vacationapp.domain.manager.LocalUserManager
import com.cromulent.vacationapp.domain.repository.OpenWeatherMapRepository
import com.cromulent.vacationapp.domain.repository.VacationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ManagerModule::class]
)
object TestAppModule {

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

    // Mock all the missing dependencies
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

    @Provides
    @Singleton
    fun provideLocalUserManager(): LocalUserManager {
        return mockk(relaxed = true)
    }



}