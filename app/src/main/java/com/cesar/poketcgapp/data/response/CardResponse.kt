package com.cesar.poketcgapp.data.response

import com.cesar.poketcgapp.presentation.model.CardModel
import com.google.gson.annotations.SerializedName

data class CardResponse (
    @SerializedName("id") val id : String,
    @SerializedName("name") val name : String,
    @SerializedName("image") val images : String,
    @SerializedName("localId") val localId : String,
) {


    fun toPresentation() : CardModel {
        return CardModel(
            id = id,
            name = name,
            image = "$images/high.png"

        )
    }
}

