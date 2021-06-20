package com.linminphyo.meditation.movie.details.network

import com.linminphyo.meditation.movie.details.MovieDetails
import retrofit2.Response
import retrofit2.http.GET

interface MovieDetailsApi {
    @GET("/3/movie/76341")
    suspend fun getMovieDetails() : Response<MovieDetails>
}