package com.example.cineconnect.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cineconnect.model.LoginRequest
import com.example.cineconnect.model.LoginResponse
import com.example.cineconnect.model.RegisterRequest
import com.example.cineconnect.model.RegisterResponse
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel: ViewModel() {
    private val userRepo = UserRepository()
    val loginResult: MutableLiveData<BaseResponse<LoginResponse>> = MutableLiveData()
    val registerResult: MutableLiveData<BaseResponse<RegisterResponse>> = MutableLiveData()

    fun login(usernameOrEmail: String, password: String) {
        loginResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(
                    password = password,
                    usernameOrEmail = usernameOrEmail
                )
                val response = userRepo.loginUser(loginRequest = loginRequest)
                if (response?.code() == 200) {
                    loginResult.value = BaseResponse.Success(response.body())
                } else {
                    loginResult.value = BaseResponse.Error(response?.message())
                }
            }
            catch (e: Exception) {
                loginResult.value = BaseResponse.Error(e.message)
            }
        }
    }

    fun register(username: String, email: String, password: String){
        registerResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val registerRequest = RegisterRequest(
                    username = username,
                    email = email,
                    password = password
                )
                val response = userRepo.registerUser(registerRequest = registerRequest)
                if (response != null) {
                    Log.d("Response123", response.body().toString())

                }
                if (response?.code() == 201) {
                    registerResult.value = BaseResponse.Success(response.body())
                } else {
                    registerResult.value = BaseResponse.Error(response?.message())
                }
            }
            catch (e: Exception) {
                registerResult.value = BaseResponse.Error(e.message)
            }
        }
    }
}