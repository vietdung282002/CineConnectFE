package com.example.cineconnect.repository

import com.example.cineconnect.model.ConFirmPasscode
import com.example.cineconnect.model.CustomResponse
import com.example.cineconnect.model.FollowResponse
import com.example.cineconnect.model.LoginRequest
import com.example.cineconnect.model.LoginResponse
import com.example.cineconnect.model.RegisterRequest
import com.example.cineconnect.model.RegisterResponse
import com.example.cineconnect.model.ResetPassword
import com.example.cineconnect.model.ResetPasswordRequest
import com.example.cineconnect.model.UpdatePassword
import com.example.cineconnect.model.UpdateResponse
import com.example.cineconnect.model.UpdateUser
import com.example.cineconnect.model.User
import com.example.cineconnect.network.API
import retrofit2.Response

class UserRepository {
    suspend fun loginUser(loginRequest: LoginRequest): Response<LoginResponse> {
        return API.apiService.loginUser(loginRequest = loginRequest)
    }

    suspend fun registerUser(registerRequest: RegisterRequest): Response<RegisterResponse> {
        return API.apiService.registerUser(registerRequest = registerRequest)
    }

    suspend fun getUser(token: String?, userId: Int):Response<User>{
        return API.apiService.getUser(token,userId)
    }

    suspend fun follow(token: String?, userId: Int): Response<FollowResponse> {
        return API.apiService.follow(token, userId)
    }

    suspend fun logout(token: String): Response<CustomResponse> {
        return API.apiService.logout(token)
    }

    suspend fun updateUserProfile(
        token: String,
        updateUser: UpdateUser,
        userId: Int
    ): Response<UpdateUser> {
        return API.apiService.updateProfile(token, updateUser, userId)
    }

    suspend fun updatePassword(
        token: String,
        updatePassword: UpdatePassword
    ): Response<UpdateResponse> {
        return API.apiService.updatePassword(token, updatePassword)
    }

    suspend fun resetPasswordRequest(
        resetPasswordRequest: ResetPasswordRequest
    ): Response<CustomResponse> {
        return API.apiService.resetPasswordRequest(resetPasswordRequest)
    }

    suspend fun confirmPasscode(
        conFirmPasscode: ConFirmPasscode
    ): Response<CustomResponse> {
        return API.apiService.confirmPasscode(conFirmPasscode)
    }

    suspend fun resetPassword(
        resetPassword: ResetPassword
    ): Response<CustomResponse> {
        return API.apiService.resetPassword(resetPassword)
    }
}