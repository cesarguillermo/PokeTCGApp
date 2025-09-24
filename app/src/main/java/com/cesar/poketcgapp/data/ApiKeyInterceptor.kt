package com.cesar.poketcgapp.data

import android.util.Log
import com.cesar.poketcgapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor (private val apiKey : String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader("X-Api-Key", apiKey)
            .build()


        return chain.proceed(newRequest)
    }
}