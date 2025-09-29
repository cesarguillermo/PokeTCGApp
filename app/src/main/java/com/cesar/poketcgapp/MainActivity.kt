package com.cesar.poketcgapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cesar.poketcgapp.presentation.cardlist.screens.CardDetailScreen

import com.cesar.poketcgapp.presentation.cardlist.screens.CardListScreen
import com.cesar.poketcgapp.ui.theme.PokeTCGAppTheme

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokeTCGAppTheme {
                PokeTCGApp()
            }
        }
    }
}

@Composable
fun PokeTCGApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "card_list"
    ) {
        composable("card_list") {
            CardListScreen(
                onCardClick = { cardId ->
                    navController.navigate("card_detail/$cardId")
                }
            )
        }

        composable(
            route = "card_detail/{cardId}",
            arguments = listOf(navArgument("cardId") { type = NavType.StringType })
        ) { backStackEntry ->
            val cardId = backStackEntry.arguments?.getString("cardId") ?: return@composable
            CardDetailScreen(
                cardId = cardId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

