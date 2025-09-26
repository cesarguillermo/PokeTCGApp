package com.cesar.poketcgapp.presentation.cardlist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.cesar.poketcgapp.presentation.model.CardModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarCards(
    textFieldState: TextFieldState,
    onSearch: (String) -> Unit,
    onClearSearch: () -> Unit,
    onBackPressed: () -> Unit,
    searchResult: LazyPagingItems<CardModel>?,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(true) } // Empieza expandida

    // Auto-expandir al aparecer
    LaunchedEffect(Unit) {
        expanded = true
    }

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
                onExpandedChange = {
                    expanded = it
                    // Si se colapsa, volver a la topbar normal
                    if (!it) {
                        onBackPressed()
                    }
                },
                placeholder = { Text("Buscar personajes") },
                leadingIcon = {
                    IconButton(
                        onClick = {
                            onBackPressed()
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                trailingIcon = {
                    if (textFieldState.text.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                textFieldState.edit { replace(0, length, "") }
                                onClearSearch()
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
        onExpandedChange = {
            expanded = it
            if (!it) {
                onBackPressed()
            }
        },
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