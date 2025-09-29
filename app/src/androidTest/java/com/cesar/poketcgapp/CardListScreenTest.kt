package com.cesar.poketcgapp

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cesar.poketcgapp.presentation.cardlist.components.SearchBarCards
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CardListScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun searchBar_displaysPlaceholder() {
        composeRule.setContent {
            SearchBarCards(
                textFieldState = rememberTextFieldState(),
                onSearch = {},
                onClearSearch = {},
                onBackPressed = {},
                searchResult = null,
                onCardClick = {}
            )
        }

        composeRule.onNodeWithText("Buscar personajes").assertIsDisplayed()
    }
    @Test
    fun searchBar_inputUpdatesTextField() {
        val textFieldState = TextFieldState()

        composeRule.setContent {
            SearchBarCards(
                textFieldState = textFieldState,
                onSearch = {},
                onClearSearch = {},
                onBackPressed = {},
                searchResult = null,
                onCardClick = {}
            )
        }

        composeRule.onNodeWithText("Buscar personajes")
            .performTextInput("Pikachu")

        assertEquals("Pikachu", textFieldState.text.toString())
    }

    // Verificar que al hacer click en el leading icon se llama onBackPressed
    @Test
    fun searchBar_leadingIconClick_callsOnBackPressed() {
        var backPressedCalled = false

        composeRule.setContent {
            SearchBarCards(
                textFieldState = rememberTextFieldState(),
                onSearch = {},
                onClearSearch = {},
                onBackPressed = { backPressedCalled = true },
                searchResult = null,
                onCardClick = {}
            )
        }

        // Click en el icono de "volver"
        composeRule.onNodeWithContentDescription("Volver").performClick()

        assertTrue(backPressedCalled)
    }

    // Verificar que el trailing icon limpia el texto
    @Test
    fun searchBar_trailingIconClearsText() {
        val textFieldState = TextFieldState()
        textFieldState.edit { replace(0, 0, "Bulbasaur") }

        var clearCalled = false

        composeRule.setContent {
            SearchBarCards(
                textFieldState = textFieldState,
                onSearch = {},
                onClearSearch = { clearCalled = true },
                onBackPressed = {},
                searchResult = null,
                onCardClick = {}
            )
        }

        // Click en icono de limpiar
        composeRule.onNodeWithContentDescription("Limpiar búsqueda").performClick()

        // Verificar que se limpió el estado
        assertEquals("", textFieldState.text.toString())
        assertTrue(clearCalled)
    }

}