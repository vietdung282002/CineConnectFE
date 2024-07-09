package com.example.cineconnect.model.entities

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
    @SerializedName("message")
    var message: String,
) {
    data class Data(
        @SerializedName("token")
        var token: String,
        @SerializedName("id")
        var id: String,
    )
}

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
    @SerializedName("message")
    var message: String,
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

data class CustomResponse(
    @SerializedName("status")
    var status: String,
    @SerializedName("message")
    var message: String,
)

data class ResetPasswordRequest(
    @SerializedName("email")
    var email: String,
)

data class ConFirmPasscode(
    @SerializedName("email")
    var email: String,
    @SerializedName("passcode")
    var passcode: String,
)

data class ResetPassword(
    @SerializedName("newPassword")
    var password: String,
    @SerializedName("email")
    var email: String,
)