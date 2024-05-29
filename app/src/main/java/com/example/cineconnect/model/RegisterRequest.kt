package com.example.cineconnect.model

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("username")
    var username: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String
)

data class RegisterResponse(
    @SerializedName("status")
    var status: String,
    @SerializedName("data")
    var data: Data,
) {
    data class Data(
        @SerializedName("id")
        var id: String,
        @SerializedName("username")
        var username: String,
        @SerializedName("email")
        var email: String,
    )
}