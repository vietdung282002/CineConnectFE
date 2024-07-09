package com.example.cineconnect.model.entities

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("bio") val bio: String,
    @SerializedName("profile_pic") val profilePic: String,
    @SerializedName("watched_count") val watchedCount: Int,
    @SerializedName("favourite_count") val favouriteCount: Int,
    @SerializedName("review_count") val reviewCount: Int,
    @SerializedName("rate_count") val rateCount: Int,
    @SerializedName("is_following") val isFollowing: Boolean?,
    @SerializedName("following_count") val followingCount: Int,
    @SerializedName("follower_count") val followerCount: Int
)
