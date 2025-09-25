package com.cesar.poketcgapp.presentation.cardlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cesar.poketcgapp.data.CardRepository
import com.cesar.poketcgapp.presentation.model.CardModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class CardListViewModel @Inject constructor(cardRepository: CardRepository) : ViewModel() {


    private val _searchQuery = MutableStateFlow<String?>(null)
    val searchQuery: StateFlow<String?> = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val cards: Flow<PagingData<CardModel>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isNullOrBlank()) {
                cardRepository.getAllCards()
            } else {
                cardRepository.searchCardsPager(query)
            }
        }
        .cachedIn(viewModelScope)

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}