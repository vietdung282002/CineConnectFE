package com.example.cineconnect.repository

import com.example.cineconnect.model.LoginRequest
import com.example.cineconnect.model.LoginResponse
import com.example.cineconnect.model.RegisterRequest
import com.example.cineconnect.model.RegisterResponse
import com.example.cineconnect.network.API
import retrofit2.Response

class UserRepository {
    suspend fun loginUser(loginRequest: LoginRequest): Response<LoginResponse>? {
        return  API.apiService.loginUser(loginRequest = loginRequest)
    }

    suspend fun registerUser(registerRequest: RegisterRequest): Response<RegisterResponse>? {
        return  API.apiService.registerUser(registerRequest = registerRequest)
    }

}