package com.example.cineconnect.model.entities

import com.google.gson.annotations.SerializedName

data class Person(
    @SerializedName("id") val id: Int,
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("biography") val biography: String,
    @SerializedName("birthday") val birthday: String,
    @SerializedName("deathday") val deathDay: String?,
    @SerializedName("gender") val gender: String,
    @SerializedName("homepage") val homepage: String?,
    @SerializedName("known_for_department") val knownForDepartment: String,
    @SerializedName("name") val name: String,
    @SerializedName("place_of_birth") val placeOfBirth: String,
    @SerializedName("profile_path") val profilePath: String?,
    @SerializedName("movies") val movies: List<MovieList>?
)
