package com.kkp.berrydex.di

import com.kkp.berrydex.data.remote.BerryApi
import com.kkp.berrydex.repository.BerryRepository
import com.kkp.berrydex.util.Constant.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideBerryRepository(
        api : BerryApi
    ) = BerryRepository(api)


    @Singleton
    @Provides
    fun provideBerryApi(): BerryApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(BerryApi::class.java)
    }
}