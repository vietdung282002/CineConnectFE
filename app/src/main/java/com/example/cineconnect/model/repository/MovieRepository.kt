package com.example.cineconnect.model.repository

import com.example.cineconnect.model.entities.FavouriteResponse
import com.example.cineconnect.model.entities.Movie
import com.example.cineconnect.model.entities.MovieListResponse
import com.example.cineconnect.model.entities.MovieRequest
import com.example.cineconnect.model.entities.RatingRequest
import com.example.cineconnect.model.entities.RatingResponse
import com.example.cineconnect.model.entities.Recommend
import com.example.cineconnect.model.entities.ReviewRequest
import com.example.cineconnect.model.entities.ReviewResponse
import com.example.cineconnect.model.entities.WatchResponse
import com.example.cineconnect.model.network.API
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

    suspend fun getUserFavoriteMovie(page: Int, id: Int): Response<MovieListResponse> {
        return API.apiService.getUserFavoriteMovie(page, id)
    }

    suspend fun getUserWatchedMovie(page: Int, id: Int): Response<MovieListResponse> {
        return API.apiService.getUserWatchedMovie(page, id)
    }

    suspend fun rateMovie(token: String, ratingRequest: RatingRequest): Response<RatingResponse> {
        return API.apiService.rateMovie(token, ratingRequest)
    }

    suspend fun watch(token: String, movieRequest: MovieRequest): Response<WatchResponse> {
        return API.apiService.watch(token, movieRequest)
    }

    suspend fun favourite(token: String, movieRequest: MovieRequest): Response<FavouriteResponse> {
        return API.apiService.favourite(token, movieRequest)
    }

    suspend fun addReview(token: String, reviewRequest: ReviewRequest): Response<ReviewResponse> {
        return API.apiService.addReview(token, reviewRequest)
    }

    suspend fun addRecommend(token: String, recommend: Recommend): Response<Unit> {
        return API.apiService.addRecommend(token, recommend)
    }
}