package com.cesar.poketcgapp.presentation.cardlist

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.cesar.poketcgapp.presentation.model.CardModel


@Composable
fun CardListScreen(cardListViewModel: CardListViewModel = hiltViewModel()) {

    val cards = cardListViewModel.cards.collectAsLazyPagingItems()

    when {
        cards.loadState.refresh is LoadState.Loading && cards.itemCount == 0 -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp), color = Color.White
                )
            }
        }

        cards.loadState.refresh is LoadState.NotLoading && cards.itemCount == 0 -> {
            Text(text = "Todavía no hay personajes")
        }

        cards.loadState.hasError -> {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Red), contentAlignment = Alignment.Center
            ) {
                Text(text = "Ha ocurrido un error")
            }
        }

        else -> {
            CardCollectionGrid(
                modifier = Modifier.padding(8.dp),
                cards = cards,
            )

            if (cards.loadState.append is LoadState.Loading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(64.dp), color = Color.White
                    )
                }
            }

        }


    }

}

@Composable
fun CardCollectionGrid(
    modifier: Modifier = Modifier,
    cards: LazyPagingItems<CardModel>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(cards.itemCount) {
            cards[it]?.let { cardModel ->
                CardItem(
                    modifier,
                    cardModel
                )


            }
        }
    }


}

@Composable
fun CardItem(
    modifier: Modifier = Modifier,
    cardModel: CardModel
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 4.dp,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.7f)
            .clip(MaterialTheme.shapes.medium)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                model = cardModel.image,
                contentDescription = "card image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(MaterialTheme.shapes.medium)

            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = cardModel.name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()

            )


        }

    }

}