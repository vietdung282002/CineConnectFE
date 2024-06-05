package com.example.cineconnect.network

import com.example.cineconnect.model.LoginRequest
import com.example.cineconnect.model.LoginResponse
import com.example.cineconnect.model.Movie
import com.example.cineconnect.model.MovieListResponse
import com.example.cineconnect.model.PeopleListResponse
import com.example.cineconnect.model.Person
import com.example.cineconnect.model.RegisterRequest
import com.example.cineconnect.model.RegisterResponse
import com.example.cineconnect.model.Review
import com.example.cineconnect.model.ReviewListResponse
import com.example.cineconnect.model.User
import com.example.cineconnect.model.UserLikedMovieResponse
import com.example.cineconnect.model.UserListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @Headers(
        "Content-Type: application/json",
        "accept: application/json",
    )
    @POST("/authentication/login/")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @Headers(
        "Content-Type: application/json",
        "accept: application/json",
    )
    @POST("/authentication/register/")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @Headers(
        "accept: application/json",
    )
    @GET("movie/")
    suspend fun movieList(
        @Query("page") page: Int,
    ): Response<MovieListResponse>

    @Headers(
        "accept: application/json",
    )
    @GET("movie/{id}/")
    suspend fun getMovie(
        @Header("Authorization") token: String?,
        @Path("id") movieId: String
    ): Response<Movie>

    @Headers(
        "accept: application/json",
    )
    @GET("person/{id}/")
    suspend fun getPerson(@Path("id") personId: String): Response<Person>

    @Headers(
        "accept: application/json",
    )
    @GET("movie/genre/")
    suspend fun getMovieListByGenre(
        @Query("page") page: Int,
        @Query("q") genreId: String
    ): Response<MovieListResponse>

    @Headers(
        "accept: application/json",
    )
    @GET("movie/search/")
    suspend fun getSearchMovie(
        @Query("page") page: Int,
        @Query("q") query: String,
    ): Response<MovieListResponse>

    @Headers(
        "accept: application/json",
    )
    @GET("person/search/")
    suspend fun getSearchPerson(
        @Query("page") page: Int,
        @Query("q") query: String,
    ): Response<PeopleListResponse>


    @Headers(
        "accept: application/json",
    )
    @GET("favourite/")
    suspend fun getListUserLikeMovie(
        @Query("page") page: Int,
        @Query("movie") movie: Int,
    ): Response<UserLikedMovieResponse>

    @Headers(
        "accept: application/json",
    )
    @GET("review/")
    suspend fun getReviewList(
        @Query("page") page: Int,
        @Query("movie") movie: Int,
    ): Response<ReviewListResponse>


    @Headers(
        "accept: application/json",
    )
    @GET("review/search/")
    suspend fun getSearchReviewList(
        @Query("page") page: Int,
        @Query("q") query: String,
    ): Response<ReviewListResponse>

    @Headers(
        "accept: application/json",
    )
    @GET("review/{id}/")
    suspend fun getReviewDetail(
        @Path("id") reviewId: Int
    ): Response<Review>

    @Headers(
        "accept: application/json",
    )
    @GET("user/{id}/")
    suspend fun getUser(
        @Header("Authorization") token: String?,
        @Path("id") userId: Int
    ): Response<User>

    @Headers(
        "accept: application/json",
    )
    @GET("user/search/")
    suspend fun getSearchUser(
        @Header("Authorization") token: String?,
        @Query("page") page: Int,
        @Query("q") query: String
    ): Response<UserListResponse>
}