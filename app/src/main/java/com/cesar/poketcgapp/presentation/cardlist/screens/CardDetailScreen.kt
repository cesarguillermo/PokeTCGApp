package com.cesar.poketcgapp.presentation.cardlist.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cesar.poketcgapp.presentation.cardlist.CardDetailViewModel
import com.cesar.poketcgapp.presentation.cardlist.components.CardHeaderSection
import com.cesar.poketcgapp.presentation.cardlist.components.CardInfoSection
import com.cesar.poketcgapp.presentation.cardlist.components.PriceSection


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDetailScreen(
    cardId: String,
    cardDetailViewModel: CardDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val cardDetail by cardDetailViewModel.cardDetail.collectAsState()
    val isLoading by cardDetailViewModel.isLoading.collectAsState()
    val error by cardDetailViewModel.error.collectAsState()

    LaunchedEffect(cardId) {
        cardDetailViewModel.loadCardDetail(cardId)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "TCG Market ", //
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )

            )
        }
    ) { paddingValues ->
        when {
            isLoading && cardDetail == null -> {
                // Estado de carga inicial
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(64.dp)
                    )
                }
            }
            error != null -> {
                // Estado de error
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = error ?: "Error desconocido",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { cardDetailViewModel.loadCardDetail(cardId) }) {
                            Text("Reintentar")
                        }
                    }
                }
            }
            cardDetail != null -> {
                // Mostrar contenido
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    cardDetail?.let { detail ->
                        // Header con imagen principal
                        CardHeaderSection(detail)

                        Spacer(modifier = Modifier.height(16.dp))

                        // Información general
                        CardInfoSection(detail)

                        Spacer(modifier = Modifier.height(16.dp))

                        // Precio
                        detail.marketPrice?.let { price ->
                            PriceSection(price, detail.currency ?: "€")
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }

    }

}

