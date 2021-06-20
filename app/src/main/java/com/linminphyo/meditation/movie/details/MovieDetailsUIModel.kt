package com.linminphyo.meditation.movie.details

data class MovieDetailsUIModel(
    val movieCoverUrl: String,
    val castProfileUrls: List<String>,
    val movieName: String,
    val totalRunTimeMin: Int,
    val year: Int,
    val rating: Float,
    val category: String,
    val summary: String
)

sealed class MovieDetailsViewState {
    object Loading: MovieDetailsViewState()
    data class Content(val data : MovieDetailsUIModel): MovieDetailsViewState()
}