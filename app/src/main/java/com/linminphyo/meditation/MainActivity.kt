package com.linminphyo.meditation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.linminphyo.meditation.movie.details.MovieDetailsScreen
import com.linminphyo.meditation.movie.details.MovieDetailsUIModel
import com.linminphyo.meditation.movie.details.MovieDetailsViewModel
import com.linminphyo.meditation.swipe.CardSwipe2
import com.linminphyo.meditation.swipe.CardSwipeScreen
import com.linminphyo.meditation.ui.theme.MeditationTheme

class MainActivity : ComponentActivity() {
    private val movieDetailsViewModel by viewModels<MovieDetailsViewModel>()

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MeditationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    CardSwipe2()
                }
            }
        }

        movieDetailsViewModel.getMovieDetails()
    }
}

@Composable
fun Greeting(name: String) {
    Button(onClick = { }) {
        Row {
            Text(text = "Click me")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MeditationTheme {
        Greeting("Android")
    }
}