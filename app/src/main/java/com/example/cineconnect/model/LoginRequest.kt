package com.example.cineconnect.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username_or_email")
    var usernameOrEmail: String,
    @SerializedName("password")
    var password: String
)

data class LoginResponse(
    @SerializedName("status")
    var status: String,
    @SerializedName("data")
    var data: Data,
) {
    data class Data(
        @SerializedName("token")
        var token: String,
        @SerializedName("id")
        var id: String,
    )
}