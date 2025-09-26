package com.cesar.poketcgapp.data.service

import com.cesar.poketcgapp.data.response.CardInfoDetailResponse
import com.cesar.poketcgapp.data.response.CardResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path
interface CardTCGApiService {

    @GET("v2/en/cards")
    suspend fun getCards(
        @Query("pagination:page") page: Int,
        @Query("pagination:itemsPerPage") pageSize: Int
    ): List<CardResponse>

    @GET("v2/en/cards")
    suspend fun searchCardsByName(
        @Query("name") name: String,
        @Query("pagination:page") page: Int,
        @Query("pagination:itemsPerPage") pageSize: Int
    ): List<CardResponse>


    @GET("v2/en/cards/{id}")
    suspend fun getCardDetail (
        @Path("id") id : String
    ) : CardInfoDetailResponse


}