package com.linminphyo.meditation

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import com.linminphyo.meditation.testing.SimpleToggleButton
import org.junit.Rule
import org.junit.Test

class MyComposeTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    // createComposeRule() if you don't need access to the activityTestRule

    @Test
    fun TestFavoriteButton() {
        // Start the app
        composeTestRule.setContent {
            MaterialTheme {
                SimpleToggleButton()
            }
        }

        composeTestRule.onNodeWithText("Unfavorite").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Button").performClick()
        composeTestRule.onNodeWithText("Favorite").assertIsDisplayed()
    }
}