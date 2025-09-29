package com.cesar.poketcgapp.presentation.cardlist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.cesar.poketcgapp.presentation.model.CardModel

@Composable
fun CardCollectionGrid(
    modifier: Modifier = Modifier,
    cards: LazyPagingItems<CardModel>,
    onCardClick : (CardModel) -> Unit
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(
            start = 8.dp,
            end = 8.dp,
            top = 16.dp,
            bottom = 64.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(cards.itemCount) { index ->
            cards[index]?.let { cardModel ->
                CardItem(
                    modifier = Modifier,
                    cardModel = cardModel,
                    onClick = {onCardClick(cardModel)}
                )
            }
        }
    }
}