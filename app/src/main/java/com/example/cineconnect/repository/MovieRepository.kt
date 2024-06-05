package com.example.cineconnect.repository

import com.example.cineconnect.model.Movie
import com.example.cineconnect.model.MovieListResponse
import com.example.cineconnect.network.API
import retrofit2.Response


class MovieRepository {
    suspend fun movieList(page: Int): Response<MovieListResponse> {
        return API.apiService.movieList(page)
    }

    suspend fun getMovie(token: String?, id: Int): Response<Movie> {
        return API.apiService.getMovie(token,id.toString())
    }

    suspend fun getMovieByGenre(page: Int, id: Int): Response<MovieListResponse> {
        return API.apiService.getMovieListByGenre(page, id.toString())
    }

//    suspend fun getSearchMovie(page: Int, query: String): Response<MovieListResponse> {
//        return API.apiService.getSearchMovie(page, query)
//    }
}