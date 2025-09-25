package com.cesar.poketcgapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cesar.poketcgapp.data.service.CardTCGApiService
import com.cesar.poketcgapp.presentation.model.CardModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CardRepository @Inject constructor(val api: CardTCGApiService) {

    companion object {
        const val MAX_ITEMS = 20
        const val PREFETCH_ITEMS = 4
    }

    fun getAllCards(): Flow<PagingData<CardModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = MAX_ITEMS, prefetchDistance = PREFETCH_ITEMS
            ),
            pagingSourceFactory = {
                CardPagingSource(api,"")
            }).flow
    }

    fun searchCardsPager (query : String ) : Flow<PagingData<CardModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = MAX_ITEMS, prefetchDistance = PREFETCH_ITEMS
            ),
            pagingSourceFactory = {
                CardPagingSource(api,query)
            }).flow
    }

}