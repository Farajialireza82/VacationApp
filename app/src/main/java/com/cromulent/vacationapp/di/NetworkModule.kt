package com.cromulent.vacationapp.di

import com.cromulent.vacationapp.data.remote.VacationApi
import com.cromulent.vacationapp.data.remote.OpenWeatherMapApi
import com.cromulent.vacationapp.util.Constants
import com.cromulent.vacationapp.util.Constants.TRIP_ADVISOR_URL
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