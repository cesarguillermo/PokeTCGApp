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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

    // Estado para controlar si estamos en modo búsqueda
    var isSearching by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            SearchBarCards(
                textFieldState = textFieldState,
                onSearch = { query ->
                    searchQuery = query
                    isSearching = query.isNotBlank()
                    if (query.isNotBlank()) {
                        cardListViewModel.updateSearchQuery(query)
                    }
                },
                onClearSearch = {
                    isSearching = false
                    searchQuery = ""
                    textFieldState.edit {  replace(0, length, "")  }
                    cardListViewModel.updateSearchQuery("") // Limpiar búsqueda
                },
                searchResult = if (isSearching) cards else null,
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { paddingValues ->

        // Contenido principal que siempre se muestra (cuando no hay búsqueda activa)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                cards.loadState.refresh is LoadState.Loading && cards.itemCount == 0 -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(64.dp),
                            color = Color.White
                        )
                    }
                }

                cards.loadState.refresh is LoadState.NotLoading && cards.itemCount == 0 -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isSearching) "No se encontraron resultados para \"$searchQuery\""
                            else "Todavía no hay personajes"
                        )
                    }
                }

                cards.loadState.hasError -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Ha ocurrido un error")
                    }
                }

                else -> {
                    CardCollectionGrid(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        cards = cards,
                    )

                    // Loading indicator para append
                    if (cards.loadState.append is LoadState.Loading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(32.dp),
                                color = Color.White
                            )
                        }
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
                    cardModel = cardModel
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarCards(
    textFieldState: TextFieldState,
    onSearch: (String) -> Unit,
    onClearSearch: () -> Unit,
    searchResult: LazyPagingItems<CardModel>?,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    SearchBar(
        modifier = modifier,
        inputField = {
            SearchBarDefaults.InputField(
                query = textFieldState.text.toString(),
                onQueryChange = { newText ->
                    textFieldState.edit { replace(0, length, newText) }
                    // Si el texto está vacío, limpiar búsqueda
                    if (newText.isBlank()) {
                        onClearSearch()
                    }
                },
                onSearch = { query ->
                    if (query.isNotBlank()) {
                        onSearch(query)
                    } else {
                        onClearSearch()
                    }
                    expanded = false
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text("Buscar") },
                trailingIcon = {
                    if (textFieldState.text.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                onClearSearch()
                                expanded = false
                            }
                        ) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Limpiar búsqueda"
                            )
                        }
                    }
                }
            )
        },
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        // Solo mostrar contenido de búsqueda si hay resultados de búsqueda
        searchResult?.let { results ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(results.itemCount) { index ->
                    val card = results[index]
                    if (card != null) {
                        CardItem(Modifier, card)
                    }
                }

                results.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }

                        loadState.append is LoadState.Error || loadState.refresh is LoadState.Error -> {
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
        } ?: run {
            // Contenido por defecto cuando no hay búsqueda
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Escribe para buscar personajes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}