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
    val cardDetail: StateFlow<CardDetailModel?> = _cardDetail

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadCardDetail(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                _cardDetail.value = cardRepository.getCardDetail(id)
            } catch (e: Exception) {
                _error.value = "Error al cargar los detalles: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}