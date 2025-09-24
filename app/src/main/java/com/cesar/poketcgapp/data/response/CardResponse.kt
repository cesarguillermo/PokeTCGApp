package com.cesar.poketcgapp.data.response

import com.google.gson.annotations.SerializedName

data class CardResponse (
    @SerializedName("id") val id : String,
    @SerializedName("name") val name : String,
    @SerializedName("images") val images : CardImagesResponse
)

data class CardImagesResponse (
    @SerializedName("small") val small : String,
    @SerializedName("large") val large : String
)