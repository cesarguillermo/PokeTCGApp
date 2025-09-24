package com.cesar.poketcgapp.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cesar.poketcgapp.data.service.CardTCGApiService
import com.cesar.poketcgapp.presentation.model.CardModel
import okio.IOException
import javax.inject.Inject

class CardPagingSource @Inject constructor(private val api: CardTCGApiService) :
    PagingSource<Int, CardModel>() {
    override fun getRefreshKey(state: PagingState<Int, CardModel>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CardModel> {
        return try {
            val currentPage = params.key ?: 1
            val pageSize = params.loadSize

            // API call
            val response = api.getCards(page = currentPage, pageSize = pageSize)
            Log.i("cesar", response.toString())
            val cards = response.cards.map {
                CardModel(
                    id = it.id,
                    name = it.name,
                    image = it.images.large
                )
            }

            //  Calculate the next and previous pages
            val nextKey =
                if (currentPage * pageSize >= response.totalCount) null else currentPage + 1
            val prevKey = if (currentPage == 1) null else currentPage - 1

            LoadResult.Page(
                data = cards,
                prevKey = prevKey,
                nextKey = nextKey

            )


        } catch (exception: Exception) {
            LoadResult.Error(exception)

        }
    }


}