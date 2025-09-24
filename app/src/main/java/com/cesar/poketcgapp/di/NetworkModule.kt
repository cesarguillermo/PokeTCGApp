package com.cesar.poketcgapp.di

import com.cesar.poketcgapp.data.ApiKeyInterceptor
import com.cesar.poketcgapp.data.service.CardTCGApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.cesar.poketcgapp.BuildConfig
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
/*
   Configuration global objects for hilt
 */
object NetworkModule {

    private const val BASE_URL = "https://api.pokemontcg.io/"

    @Provides
    fun provideApiService(retrofit: Retrofit): CardTCGApiService =
        retrofit.create(CardTCGApiService::class.java)

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient).build()

    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // conexión
            .readTimeout(60, TimeUnit.SECONDS)    // lectura
            .writeTimeout(30, TimeUnit.SECONDS)   // escritura
            .addInterceptor(ApiKeyInterceptor(BuildConfig.POKETCG_API_KEY))
            .build()
}