package com.example.cineconnect.model

import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("id") val id: Int,
    @SerializedName("movie") val movie: MovieList,
    @SerializedName("user") val user: UserList,
    @SerializedName("content") val content: String,
    @SerializedName("rating") val rating: Float,
    @SerializedName("favourite") val favourite: Boolean,
    @SerializedName("likes_count") val likesCount: Int,
    @SerializedName("comment_count") val commentCount: Int,
    @SerializedName("is_liked") val isLiked: Boolean,
    @SerializedName("watched_day") val watchedDay: String
)
