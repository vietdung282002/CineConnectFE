package com.example.cineconnect.model

import com.google.gson.annotations.SerializedName

data class RatingRequest(
    @SerializedName("movie") val movieId: Int,
    @SerializedName("rate") val rating: Float
)

data class RatingResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: RatingDetails
)

data class RatingDetails(
    @SerializedName("movie") val movieId: Int,
    @SerializedName("user") val userId: Int,
    @SerializedName("rate") val rating: Rating
)