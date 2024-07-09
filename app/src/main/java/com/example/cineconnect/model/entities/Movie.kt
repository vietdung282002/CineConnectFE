package com.example.cineconnect.model.entities

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
    @SerializedName("rating") val rating: Rating,
    @SerializedName("is_watched") val isWatched: Boolean,
    @SerializedName("is_favourite") val isFavourite: Boolean
)

data class Rating(
    @SerializedName("avr") val avr: Avr,
    @SerializedName("total") val total: Int,
    @SerializedName("rating") val rating: Map<String, Int>,
    @SerializedName("user_rating") var userRating: Float?,
)

data class Avr(
    @SerializedName("avg_rate") val rateAvg: Float
)

data class MovieRequest(
    @SerializedName("movie") val movieId: Int,
)

data class WatchResponse(
    @SerializedName("status")
    var status: String,
    @SerializedName("message")
    var message: Message,
) {
    data class Message(
        @SerializedName("movie")
        var movie: Int,
        @SerializedName("user")
        var user: Int,
        @SerializedName("watched")
        var watched: Boolean,
    )
}

data class FavouriteResponse(
    @SerializedName("status")
    var status: String,
    @SerializedName("message")
    var message: Message,
) {
    data class Message(
        @SerializedName("movie")
        var movie: Int,
        @SerializedName("user")
        var user: Int,
        @SerializedName("favourite")
        var favourite: Boolean,
    )
}


