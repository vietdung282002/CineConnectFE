package com.example.cineconnect.repository

import com.example.cineconnect.model.Movie
import com.example.cineconnect.model.MovieListResponse
import com.example.cineconnect.network.API
import retrofit2.Response


class MovieRepository {
    suspend fun movieList(): Response<MovieListResponse>{
        return API.apiService.movieList()
    }

    suspend fun getMovie(id: Int): Response<Movie>{
        return API.apiService.getMovie(id.toString())
    }

    suspend fun getMovieByGenre(id:Int): Response<MovieListResponse>{
        return API.apiService.getMovieListByGenre(id.toString())
    }
}