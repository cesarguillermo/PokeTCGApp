package com.cesar.poketcgapp.presentation.model

class CardDetailModel (
    val id: String,
    val name: String,
    val localId: String,
    val setName: String,
    val totalCards: Int,
    val symbolUrl: String?,
    val imageUrl: String,
    val marketPrice: Double?,
    val currency: String?
)