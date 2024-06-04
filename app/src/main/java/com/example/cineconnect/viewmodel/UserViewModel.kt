package com.example.cineconnect.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.cineconnect.model.FavouriteList
import com.example.cineconnect.model.LoginRequest
import com.example.cineconnect.model.LoginResponse
import com.example.cineconnect.model.RegisterRequest
import com.example.cineconnect.model.RegisterResponse
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.paging.FavouriteUserPagingSource
import com.example.cineconnect.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val userRepo = UserRepository()
    val loginResult: MutableLiveData<BaseResponse<LoginResponse>> = MutableLiveData()
    val registerResult: MutableLiveData<BaseResponse<RegisterResponse>> = MutableLiveData()
    private val _userFavouriteState =
        MutableStateFlow<BaseResponse<PagingData<FavouriteList>>>(BaseResponse.Loading())
    val userFavouriteState: StateFlow<BaseResponse<PagingData<FavouriteList>>> = _userFavouriteState
    fun login(usernameOrEmail: String, password: String) {
        loginResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(
                    password = password,
                    usernameOrEmail = usernameOrEmail
                )
                val response = userRepo.loginUser(loginRequest = loginRequest)
                if (response.isSuccessful) {
                    loginResult.value = BaseResponse.Success(response.body())
                } else {
                    loginResult.value = BaseResponse.Error(response.message())
                }
            } catch (e: Exception) {
                loginResult.value = BaseResponse.Error(e.message)
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        registerResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val registerRequest = RegisterRequest(
                    username = username,
                    email = email,
                    password = password
                )
                val response = userRepo.registerUser(registerRequest = registerRequest)
                if (response.isSuccessful) {
                    registerResult.value = BaseResponse.Success(response.body())
                } else {
                    registerResult.value = BaseResponse.Error(response.message())
                }
            } catch (e: Exception) {
                registerResult.value = BaseResponse.Error(e.message)
            }
        }
    }

    fun getFavourite(movieId: Int) {
        _userFavouriteState.value = BaseResponse.Loading()
        viewModelScope.launch {
            Pager(PagingConfig(pageSize = 10, enablePlaceholders = true)) {
                FavouriteUserPagingSource(movieId)
            }.flow
                .catch { e -> _userFavouriteState.value = BaseResponse.Error(e.message) }
                .collectLatest { pagingData ->
                    _userFavouriteState.value = BaseResponse.Success(pagingData)
                }

        }
    }
}