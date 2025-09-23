package com.cesar.poketcgapp.data.response

import com.google.gson.annotations.SerializedName

data class ResponseWrapper (

    @SerializedName("data") val cards: List<CardResponse>,
    @SerializedName("page") val page : Int,
    @SerializedName("pageSize") val pageSize : Int,
    @SerializedName("totalCount") val totalCount : Int,

)