package com.cesar.poketcgapp.presentation.cardlist.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.text.input.rememberTextFieldState


import androidx.compose.material3.CircularProgressIndicator

import androidx.compose.material3.Scaffold

import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.cesar.poketcgapp.presentation.cardlist.CardListViewModel
import com.cesar.poketcgapp.presentation.cardlist.components.AppTopBar
import com.cesar.poketcgapp.presentation.cardlist.components.CardCollectionGrid
import com.cesar.poketcgapp.presentation.cardlist.components.SearchBarCards


@Composable
fun CardListScreen(
    cardListViewModel: CardListViewModel = hiltViewModel(),
    onCardClick: (String) -> Unit
) {

    val textFieldState = rememberTextFieldState()
    val cards = cardListViewModel.cards.collectAsLazyPagingItems()

    // Estado para controlar si estamos en modo búsqueda
    var isSearching by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var showSearchBar by rememberSaveable { mutableStateOf(false) }





    Scaffold(
        topBar = {
            if (showSearchBar) {
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
                        textFieldState.edit { replace(0, length, "") }
                        cardListViewModel.updateSearchQuery("")
                    },
                    onBackPressed = {
                        showSearchBar = false
                        isSearching = false
                        searchQuery = ""
                        textFieldState.edit { replace(0, length, "") }
                        cardListViewModel.updateSearchQuery("")
                    },
                    searchResult = if (isSearching) cards else null,
                    modifier = Modifier.fillMaxWidth(),
                    onCardClick = { cardId ->
                        showSearchBar = false
                        isSearching = false
                        onCardClick(cardId)
                    }
                )
            } else {
                AppTopBar(
                    onSearchClick = { showSearchBar = true }
                )
            }
        }
    ) { paddingValues ->

        // Contenido principal que siempre se muestra
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
                        onCardClick = { card -> onCardClick(card.id)  }
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