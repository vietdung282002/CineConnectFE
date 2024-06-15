package com.example.cineconnect.model

import com.google.gson.annotations.SerializedName

data class Activity(
    @SerializedName("id") val id: Int,
    @SerializedName("type") val type: Int,
    @SerializedName("movie") val movie: MovieList?,
    @SerializedName("user") val user: UserList,
    @SerializedName("user_follow") val userFollow: UserList?,
    @SerializedName("review") val review: ReviewList?,
    @SerializedName("time_stamp") val timeStamp: String
)

data class ActivityResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val results: List<Activity>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("current_page") val currentPage: Int
)