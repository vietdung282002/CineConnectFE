package com.example.cineconnect.model.repository

import com.example.cineconnect.model.entities.ConFirmPasscode
import com.example.cineconnect.model.entities.CustomResponse
import com.example.cineconnect.model.entities.FollowResponse
import com.example.cineconnect.model.entities.LoginRequest
import com.example.cineconnect.model.entities.LoginResponse
import com.example.cineconnect.model.entities.RegisterRequest
import com.example.cineconnect.model.entities.RegisterResponse
import com.example.cineconnect.model.entities.ResetPassword
import com.example.cineconnect.model.entities.ResetPasswordRequest
import com.example.cineconnect.model.entities.UpdatePassword
import com.example.cineconnect.model.entities.UpdateResponse
import com.example.cineconnect.model.entities.UpdateUser
import com.example.cineconnect.model.entities.User
import com.example.cineconnect.model.network.API
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