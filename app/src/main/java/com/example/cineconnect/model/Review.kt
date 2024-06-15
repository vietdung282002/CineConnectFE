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
    @SerializedName("watched_day") val watchedDay: String,
    @SerializedName("time_stamp") val timeStamp: String
)

data class ReviewRequest(
    @SerializedName("movie") val movie: Int,
    @SerializedName("content") val content: String,
)

data class ReviewResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: ReviewRequest
)

data class LikeResponse(
    @SerializedName("status") val status: String,
    @SerializedName("result") val result: Like
)

data class Like(
    @SerializedName("review") val review: Int,
    @SerializedName("like") val like: Boolean,
    @SerializedName("number_of_like") val numberOfLike: Int
)


data class CommentRequest(
    @SerializedName("review") val review: Int,
    @SerializedName("comment") val comment: String
)

