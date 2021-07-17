package com.linminphyo.meditation.testing

import android.text.Layout
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SimpleToggleButton() {
    var isFavorite by remember {
        mutableStateOf(true)
    }

    Button(onClick = { isFavorite = !isFavorite }, Modifier.testTag("Button")) {
        Row(modifier = Modifier.align(Alignment.CenterVertically)) {
            Icon(
                if (isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorite"
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(if (isFavorite) "Unfavorite" else "Favorite")
        }
    }
}


@Composable
@Preview
fun SimpleToggleButtonPreview() {
    SimpleToggleButton()
}