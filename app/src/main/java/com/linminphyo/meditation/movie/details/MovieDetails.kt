package com.linminphyo.meditation.movie.details

import com.squareup.moshi.Json

data class MovieDetails(
    val id: String,
    @Json(name = "backdrop_path") val backdropPath: String,
    @Json(name = "original_title") val title: String,
    @Json(name = "overview") val overview: String,
    @Json(name = "vote_average") val voteAverage: Float,
)