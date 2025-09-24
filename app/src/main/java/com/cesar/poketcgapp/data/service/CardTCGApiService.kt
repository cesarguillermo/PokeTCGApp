package com.cesar.poketcgapp.data.service

import com.cesar.poketcgapp.data.response.ResponseWrapper
import retrofit2.http.GET
import retrofit2.http.Query

interface CardTCGApiService {

    @GET("/v2/cards")
    suspend fun getCards(@Query("page") page : Int, @Query("pageSize") pageSize : Int) : ResponseWrapper



}