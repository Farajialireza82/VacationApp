package com.cromulent.vacationapp.di

import android.app.Application
import androidx.room.Room
import com.cromulent.vacationapp.data.local.LocationCacheDao
import com.cromulent.vacationapp.data.manager.LocalUserManagerImpl
import com.cromulent.vacationapp.data.remote.VacationApi
import com.cromulent.vacationapp.data.local.LocationDatabase
import com.cromulent.vacationapp.data.local.LocationPhotosCacheDao
import com.cromulent.vacationapp.data.local.LocationTypeConvertor
import com.cromulent.vacationapp.data.remote.OpenWeatherMapApi
import com.cromulent.vacationapp.data.repository.GpsRepositoryImpl
import com.cromulent.vacationapp.data.repository.OpenWeatherMapRepositoryImpl
import com.cromulent.vacationapp.data.repository.VacationRepositoryImpl
import com.cromulent.vacationapp.domain.manager.GpsRepository
import com.cromulent.vacationapp.domain.manager.LocalUserManager
import com.cromulent.vacationapp.domain.repository.OpenWeatherMapRepository
import com.cromulent.vacationapp.domain.repository.VacationRepository
import com.cromulent.vacationapp.util.Constants
import com.cromulent.vacationapp.util.Constants.TRIP_ADVISOR_URL
import com.cromulent.vacationapp.util.Constants.LOCATION_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Provides
    @Singleton
    fun provideVacationApi(): VacationApi {

        return Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(TRIP_ADVISOR_URL)
            .client(client)
            .build()
            .create(VacationApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOpenWeatherMapApi(): OpenWeatherMapApi {

        return Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.OPEN_WEATHER_MAP_URL)
            .client(client)
            .build()
            .create(OpenWeatherMapApi::class.java)
    }

}