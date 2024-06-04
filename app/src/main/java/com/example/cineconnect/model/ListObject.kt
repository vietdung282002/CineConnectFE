package com.example.cineconnect.model

import com.google.gson.annotations.SerializedName

data class CastList(
    @SerializedName("id") val id: Int,
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("gender") val gender: String,
    @SerializedName("known_for_department") val knownForDepartment: String,
    @SerializedName("name") val name: String,
    @SerializedName("original_name") val originalName: String,
    @SerializedName("profile_path") val profilePath: String?,
    @SerializedName("character") val character: String,
    @SerializedName("order") val order: Int
)

data class DirectorList(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
)

data class MovieList(
    @SerializedName("id")
    var id: Int,
    @SerializedName("original_title")
    var originalTitle: String,
    @SerializedName("poster_path")
    var posterPath: String,
    @SerializedName("release_date")
    var releaseDate: String?,
    @SerializedName("directors")
    var directors: List<DirectorList>?,

    )

data class MovieListResponse(
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val movieLists: List<MovieList>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("current_page")
    val currentPage: Int,
)

data class PeopleListResponse(
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val personLists: List<Person>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("current_page")
    val currentPage: Int,
)

data class UserLikedMovieResponse(
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val favouriteLists: List<FavouriteList>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("current_page")
    val currentPage: Int,
)

data class FavouriteList(
    @SerializedName("user") val user: UserList,
    @SerializedName("rate") val rate: Double?
)

data class UserList(
    @SerializedName("id")
    var id: Int,
    @SerializedName("username")
    var username: String,
    @SerializedName("profile_pic")
    var profilePic: String,
)

data class ReviewListResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val reviewLists: List<ReviewList>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("current_page") val currentPage: Int
)

data class ReviewList(
    @SerializedName("id") val id: Int,
    @SerializedName("user") val user: UserList,
    @SerializedName("rating") val rating: Int,
    @SerializedName("favourite") val favourite: Boolean,
    @SerializedName("content") val content: String
)
