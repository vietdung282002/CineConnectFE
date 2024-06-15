package com.example.cineconnect.network

import com.example.cineconnect.model.ActivityResponse
import com.example.cineconnect.model.Comment
import com.example.cineconnect.model.CommentRequest
import com.example.cineconnect.model.CommentResponse
import com.example.cineconnect.model.ConFirmPasscode
import com.example.cineconnect.model.CustomResponse
import com.example.cineconnect.model.FavouriteResponse
import com.example.cineconnect.model.FollowListResponse
import com.example.cineconnect.model.FollowResponse
import com.example.cineconnect.model.LikeResponse
import com.example.cineconnect.model.LoginRequest
import com.example.cineconnect.model.LoginResponse
import com.example.cineconnect.model.Movie
import com.example.cineconnect.model.MovieListResponse
import com.example.cineconnect.model.MovieRequest
import com.example.cineconnect.model.PeopleListResponse
import com.example.cineconnect.model.Person
import com.example.cineconnect.model.RatingRequest
import com.example.cineconnect.model.RatingResponse
import com.example.cineconnect.model.Recommend
import com.example.cineconnect.model.RegisterRequest
import com.example.cineconnect.model.RegisterResponse
import com.example.cineconnect.model.ResetPassword
import com.example.cineconnect.model.ResetPasswordRequest
import com.example.cineconnect.model.Review
import com.example.cineconnect.model.ReviewListResponse
import com.example.cineconnect.model.ReviewRequest
import com.example.cineconnect.model.ReviewResponse
import com.example.cineconnect.model.UpdatePassword
import com.example.cineconnect.model.UpdateResponse
import com.example.cineconnect.model.UpdateUser
import com.example.cineconnect.model.User
import com.example.cineconnect.model.UserLikedMovieResponse
import com.example.cineconnect.model.UserListResponse
import com.example.cineconnect.model.WatchResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
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
        "Content-Type: application/json",
        "accept: application/json",
    )
    @POST("/authentication/logout/")
    suspend fun logout(@Header("Authorization") token: String): Response<CustomResponse>

    @Headers(
        "Content-Type: application/json",
        "accept: application/json",
    )
    @PUT("/user/{id}/")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body updateUser: UpdateUser,
        @Path("id") userId: Int
    ): Response<UpdateUser>


    @Headers(
        "Content-Type: application/json",
        "accept: application/json",
    )
    @PUT("/authentication/change_password")
    suspend fun updatePassword(
        @Header("Authorization") token: String,
        @Body updatePassword: UpdatePassword
    ): Response<UpdateResponse>

    @Headers(
        "Content-Type: application/json",
        "accept: application/json",
    )
    @POST("/authentication/request_password_update")
    suspend fun resetPasswordRequest(
        @Body resetPasswordRequest: ResetPasswordRequest
    ): Response<CustomResponse>

    @Headers(
        "Content-Type: application/json",
        "accept: application/json",
    )
    @POST("/authentication/confirm_user_passcode")
    suspend fun confirmPasscode(
        @Body conFirmPasscode: ConFirmPasscode
    ): Response<CustomResponse>

    @Headers(
        "Content-Type: application/json",
        "accept: application/json",
    )
    @PUT("/authentication/reset_password")
    suspend fun resetPassword(
        @Body resetPassword: ResetPassword
    ): Response<CustomResponse>


    @Headers(
        "Content-Type: application/json",
        "accept: application/json",
    )
    @POST("/rate/")
    suspend fun rateMovie(
        @Header("Authorization") token: String,
        @Body rateRequest: RatingRequest
    ): Response<RatingResponse>

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
    @GET("movie/favourite/")
    suspend fun getUserFavoriteMovie(
        @Query("page") page: Int,
        @Query("q") userId: Int
    ): Response<MovieListResponse>

    @Headers(
        "accept: application/json",
    )
    @GET("activity/")
    suspend fun getActivity(
        @Query("page") page: Int,
        @Query("user") userId: Int
    ): Response<ActivityResponse>

    @Headers(
        "accept: application/json",
    )
    @GET("movie/watched/")
    suspend fun getUserWatchedMovie(
        @Query("page") page: Int,
        @Query("q") userId: Int
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
    @GET("review/")
    suspend fun getReviewListByUser(
        @Query("page") page: Int,
        @Query("user") user: Int,
    ): Response<ReviewListResponse>

    @Headers(
        "accept: application/json",
    )
    @GET("review/newsfeed/")
    suspend fun getReviewNewFeed(
        @Query("page") page: Int,
        @Header("Authorization") token: String?,
    ): Response<ReviewListResponse>

    @Headers(
        "accept: application/json",
    )
    @GET("review/recommend")
    suspend fun getReviewRecommend(
        @Query("page") page: Int,
        @Header("Authorization") token: String?,
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
    @GET("review/comment/")
    suspend fun getReviewCommentList(
        @Query("page") page: Int,
        @Query("review") reviewId: Int,
    ): Response<CommentResponse>

    @Headers(
        "accept: application/json",
    )
    @GET("review/{id}/")
    suspend fun getReviewDetail(
        @Header("Authorization") token: String?,
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


    @Headers(
        "accept: application/json",
    )
    @POST("review/reaction/like/")
    suspend fun like(
        @Header("Authorization") token: String?,
        @Query("review") reviewId: Int
    ): Response<LikeResponse>

    @Headers(
        "accept: application/json",
    )
    @POST("follow/toggle/")
    suspend fun follow(
        @Header("Authorization") token: String?,
        @Query("user") reviewId: Int
    ): Response<FollowResponse>

    @Headers(
        "accept: application/json",
    )
    @GET("follow/list_follower/")
    suspend fun getFollowerUser(
        @Header("Authorization") token: String?,
        @Query("page") page: Int,
        @Query("user") userId: Int
    ): Response<FollowListResponse>

    @Headers(
        "accept: application/json",
    )
    @GET("follow/list_following/")
    suspend fun getFollowingUser(
        @Header("Authorization") token: String?,
        @Query("page") page: Int,
        @Query("user") reviewId: Int
    ): Response<FollowListResponse>

    @Headers(
        "accept: application/json",
    )
    @POST("review/comment/")
    suspend fun comment(
        @Header("Authorization") token: String,
        @Body commentRequest: CommentRequest
    ): Response<UpdateResponse>

    @Headers(
        "Content-Type: application/json",
        "accept: application/json",
    )
    @POST("watched/")
    suspend fun watch(
        @Header("Authorization") token: String,
        @Body movieRequest: MovieRequest
    ): Response<WatchResponse>

    @Headers(
        "Content-Type: application/json",
        "accept: application/json",
    )
    @POST("favourite/")
    suspend fun favourite(
        @Header("Authorization") token: String,
        @Body movieRequest: MovieRequest
    ): Response<FavouriteResponse>

    @Headers(
        "Content-Type: application/json",
        "accept: application/json",
    )
    @POST("review/")
    suspend fun addReview(
        @Header("Authorization") token: String,
        @Body reviewRequest: ReviewRequest
    ): Response<ReviewResponse>

    @Headers(
        "Content-Type: application/json",
        "accept: application/json",
    )
    @PUT("review/{id}/")
    suspend fun editReview(
        @Header("Authorization") token: String,
        @Body reviewRequest: ReviewRequest,
        @Path("id") reviewId: Int
    ): Response<ReviewRequest>

    @Headers(
        "accept: */*",
    )
    @DELETE("review/{id}/")
    suspend fun deleteReview(
        @Header("Authorization") token: String,
        @Path("id") reviewId: Int
    ): Response<Unit>

    @Headers(
        "accept: application/json",
    )
    @GET("review/comment/{id}/")
    suspend fun getComment(
        @Path("id") commentId: Int
    ): Response<Comment>

    @Headers(
        "Content-Type: application/json",
        "accept: application/json",
    )
    @PUT("review/comment/{id}/")
    suspend fun editComment(
        @Header("Authorization") token: String,
        @Body commentRequest: CommentRequest,
        @Path("id") commentId: Int
    ): Response<CommentRequest>

    @Headers(
        "accept: */*",
    )
    @DELETE("review/comment/{id}/")
    suspend fun deleteComment(
        @Header("Authorization") token: String,
        @Path("id") commentId: Int
    ): Response<Unit>

    @Headers(
        "Content-Type: application/json",
        "accept: application/json",
    )
    @POST("recommend/add/")
    suspend fun addRecommend(
        @Header("Authorization") token: String,
        @Body recommend: Recommend,
    ): Response<Unit>


}