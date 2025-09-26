package com.cesar.poketcgapp.presentation.cardlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cesar.poketcgapp.data.CardRepository
import com.cesar.poketcgapp.presentation.model.CardDetailModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardDetailViewModel @Inject constructor(private val cardRepository: CardRepository) : ViewModel() {

    private val _cardDetail = MutableStateFlow<CardDetailModel?>(null)
    val cardDetail : StateFlow<CardDetailModel?> = _cardDetail

    fun loadCardDetail(id : String) {
        viewModelScope.launch {
            _cardDetail.value = cardRepository.getCardDetail(id)
        }
    }
}