package com.linminphyo.meditation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.linminphyo.meditation.testing.SimpleToggleButton
import org.junit.Rule
import org.junit.Test

class ComposeTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    // createComposeRule() if you don't need access to the activityTestRule

    @Test
    fun TestFavoriteButton() {
        // Start the app
        composeTestRule.setContent {
            SimpleToggleButton()
        }

        composeTestRule.onNodeWithText("Unfavorite").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Button").performClick()
        composeTestRule.onNodeWithText("Favorite").assertIsDisplayed()
    }
}