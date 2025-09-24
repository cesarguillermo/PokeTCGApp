package com.cesar.poketcgapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import com.cesar.poketcgapp.presentation.cardlist.CardListScreen
import com.cesar.poketcgapp.ui.theme.PokeTCGAppTheme

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokeTCGAppTheme {
                CardListScreen()
            }
        }
    }
}

