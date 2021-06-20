package com.linminphyo.meditation.movie.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.linminphyo.meditation.movie.details.network.MovieDetailsApi
import com.linminphyo.meditation.movie.details.network.RestAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

interface VMInterface {
    val movieDetailsFlow : MutableStateFlow<MovieDetailsViewState>
}

class MovieDetailsViewModel(app: Application) : AndroidViewModel(app), VMInterface {
    private val movieDetailsApi by lazy {
        RestAdapter(app).getRetrofit().create(MovieDetailsApi::class.java)
    }

    override val movieDetailsFlow : MutableStateFlow<MovieDetailsViewState> = MutableStateFlow(MovieDetailsViewState.Loading)

    fun getMovieDetails() {
        viewModelScope.launch {
            val movieDetails = movieDetailsApi.getMovieDetails()
            movieDetails.body()?.let { movie ->
                movieDetailsFlow.value = MovieDetailsViewState.Content(
                    MovieDetailsUIModel(
                        movieCoverUrl = "https://image.tmdb.org/t/p/original/${movie.backdropPath}",
                        castProfileUrls = emptyList(),
                        totalRunTimeMin = 10,
                        movieName = movie.title,
                        year = 2020,
                        rating = movie.voteAverage,
                        category = "",
                        summary = movie.overview
                    )
                )
            }

        }
    }

}