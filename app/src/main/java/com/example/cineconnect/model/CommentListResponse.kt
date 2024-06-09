package com.example.cineconnect.model

import com.google.gson.annotations.SerializedName

data class CommentResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val results: List<Comment>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("current_page") val currentPage: Int
)

data class Comment(
    @SerializedName("id") val id: Int,
    @SerializedName("user") val user: UserList,
    @SerializedName("comment") val comment: String,
    @SerializedName("time_stamp") val timeStamp: String
)
