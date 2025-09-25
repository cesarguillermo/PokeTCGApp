package com.cesar.poketcgapp.presentation.cardlist

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
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
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

    val textFieldState = rememberTextFieldState()


    val cards = cardListViewModel.cards.collectAsLazyPagingItems()


    Scaffold(
        topBar =  {
            SearchBarCards(
                textFieldState = textFieldState,
                onSearch = {query -> cardListViewModel.updateSearchQuery(query)},
                searchResult = cards,
                modifier = Modifier.fillMaxWidth()
            )

        }
    ) { paddingValues  ->
        Box(modifier = Modifier.padding(paddingValues))
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
                    modifier = Modifier
                        .padding(8.dp),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarCards(
    textFieldState: TextFieldState,
    onSearch: (String) -> Unit,
    searchResult: LazyPagingItems<CardModel>,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    query = textFieldState.text.toString(),
                    onQueryChange = { newText ->
                        textFieldState.edit { replace(0, length, newText) }
                    },
                    onSearch = {
                        onSearch(textFieldState.text.toString())
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Buscar") }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            // 👇 Resultados en grid dentro del cuerpo expandido
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = modifier.fillMaxSize()
            ) {
                items(searchResult.itemCount) { index ->
                    val card = searchResult[index]
                    if (card != null) {
                        CardItem(Modifier, card)
                    }
                }

                searchResult.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .align(Alignment.CenterHorizontally)
                                )
                            }
                        }

                        loadState.append is LoadState.Error -> {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Text(
                                    text = "Error cargando resultados",
                                    modifier = Modifier.padding(16.dp),
                                    color = Color.Red
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
