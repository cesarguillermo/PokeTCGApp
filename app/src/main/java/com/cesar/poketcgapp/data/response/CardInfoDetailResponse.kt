package com.cesar.poketcgapp.data.response

import android.util.Log
import com.cesar.poketcgapp.presentation.model.CardDetailModel
import com.google.gson.annotations.SerializedName

data class CardInfoDetailResponse(
    @SerializedName("id") val id: String,
    @SerializedName("localId") val localId: String,
    @SerializedName("image") val image : String,
    @SerializedName("name") val name: String,
    @SerializedName("set") val set: CardSetInfo,
    @SerializedName("pricing") val pricing: PricingResponse
) {

    fun toPresentation () : CardDetailModel {


        return CardDetailModel(
            id = id,
            name = name,
            localId = localId,
            setName = set.name,
            totalCards = set.cardCount.total,
            symbolUrl = "${set.symbol}.png",
            imageUrl  = "$image/high.png",
            marketPrice = pricing.cardMarket?.avg,
            currency = pricing.cardMarket?.unit
        )
    }


}


data class CardSetInfo(
    @SerializedName("id") val id : String,
    @SerializedName("name") val name: String,
    @SerializedName("symbol") val symbol: String?,
    @SerializedName("cardCount") val cardCount: CardCount
)

data class CardCount(
    @SerializedName("total") val total: Int
)

data class PricingResponse(
    @SerializedName("cardmarket") val cardMarket: CardMarket?,
    @SerializedName("tcgplayer") val tcgPlayer: TcgPlayer?
)

data class CardMarket(
    @SerializedName("avg") val avg: Double?,
    @SerializedName("unit") val unit: String?
)

data class TcgPlayer(
    @SerializedName("unit") val unit: String?,
    @SerializedName("normal") val normal: NormalMarket?,
)

data class NormalMarket(
    @SerializedName("marketPrice") val marketPrice: Double?
)
