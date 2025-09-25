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

        companion object {
            private  const val PAGE_SIZE=10
        }


    override fun getRefreshKey(state: PagingState<Int, CardModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CardModel> {
        return try {
            val currentPage = params.key ?: 1


            // API call
            val response = api.getCards(page = currentPage, pageSize = PAGE_SIZE)

            val cards = response
                .filter {
                    it.localId != "!" && it.localId != "%3F"
                }
                .map {
                it.toPresentation()
            }


            //  Calculate the next and previous pages
            val nextKey = if (cards.isEmpty()) null else currentPage + 1
            val prevKey = if (currentPage == 1) null else currentPage - 1

            LoadResult.Page(
                data = cards,
                prevKey = prevKey,
                nextKey = nextKey

            )


        } catch (exception: Exception) {
            Log.e("API_DEBUG", "Error cargando cards", exception)

            LoadResult.Error(exception)

        }
    }


}