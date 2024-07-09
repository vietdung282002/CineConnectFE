package com.example.cineconnect.model.entities

import com.google.gson.annotations.SerializedName

data class UpdateUser(
    @SerializedName("username") val username: String?,
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("last_name") val lastName: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("gender") val gender: String?,
    @SerializedName("bio") val bio: String?,
    @SerializedName("profile_pic") val profilePic: String?
)

data class UpdatePassword(
    @SerializedName("currentPassword") val currentPassword: String?,
    @SerializedName("newPassword") val newPassword: String?
)

data class UpdateResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
)