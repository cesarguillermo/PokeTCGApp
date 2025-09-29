package com.cesar.poketcgapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ComposeUiTest {

    @Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testIfNameOfTopBarIsDisplayed() {
        composeRule.onNodeWithText("TCG Market").assertIsDisplayed()
    }
}