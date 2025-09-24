package com.cesar.poketcgapp.presentation.cardlist

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.cesar.poketcgapp.data.CardRepository
import com.cesar.poketcgapp.presentation.model.CardModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CardListViewModel @Inject constructor(cardRepository: CardRepository) : ViewModel() {

    val cards : Flow <PagingData<CardModel>> = cardRepository.getAllCards()

}