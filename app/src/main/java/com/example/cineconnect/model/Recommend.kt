package com.example.cineconnect.model

import com.google.gson.annotations.SerializedName

data class Recommend(
    @SerializedName("movie") val movie: Int,
)
