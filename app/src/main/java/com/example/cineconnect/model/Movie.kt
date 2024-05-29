package com.example.cineconnect.model

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("id") val id: Int,
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("budget") val budget: Int,
    @SerializedName("homepage") val homepage: String,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("revenue") val revenue: Int,
    @SerializedName("runtime") val runtime: Int,
    @SerializedName("title") val title: String,
    @SerializedName("tagline") val tagline: String,
    @SerializedName("genres") val genres: List<Genre>,
    @SerializedName("casts") val casts: List<CastList>,
    @SerializedName("directors") val directors: List<DirectorList>,
    @SerializedName("review_count") val reviewCount: Int,
    @SerializedName("favourite_count") val favouriteCount: Int,
    @SerializedName("rating") val rating: List<Rating>,
)

data class Rating(
    @SerializedName("avr") val avr: Avr,
    @SerializedName("total") val total: Int,
    @SerializedName("rating") val rating: Map<String, Int>
)

data class Avr(
    @SerializedName("rate__avg") val rateAvg: Float
)

