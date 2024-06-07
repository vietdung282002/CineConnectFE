package com.example.cineconnect.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
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
import com.example.cineconnect.model.User
import com.example.cineconnect.model.UserList
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.paging.FavouriteUserPagingSource
import com.example.cineconnect.paging.UserPagingSource
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
    val userResult: MutableLiveData<BaseResponse<User>> = MutableLiveData()

    private val _userFavouriteState =
        MutableStateFlow<BaseResponse<PagingData<FavouriteList>>>(BaseResponse.Loading())
    val userFavouriteState: StateFlow<BaseResponse<PagingData<FavouriteList>>> = _userFavouriteState

    private val _userState =
        MutableStateFlow<BaseResponse<PagingData<UserList>>>(BaseResponse.Loading())
    val userState: StateFlow<BaseResponse<PagingData<UserList>>> = _userState

    private val _followStatus = MutableLiveData<Pair<Int, Boolean?>>()
    val followStatus: LiveData<Pair<Int, Boolean?>> = _followStatus


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

    fun getUser(token: String?,userId:Int){
        userResult.value = BaseResponse.Loading()

        viewModelScope.launch {
            try {
                val response = userRepo.getUser(token,userId)
                _followStatus.value = Pair(userId, response.body()!!.isFollowing)
                if (response.isSuccessful) {
                    userResult.value = BaseResponse.Success(response.body())
                } else {
                    userResult.value = BaseResponse.Error(response.message())
                }
            }
            catch (e:Exception){
                userResult.value = BaseResponse.Error(e.message)
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

    fun getSearchUser(token: String?,query: String){
        _userState.value = BaseResponse.Loading()

        viewModelScope.launch {
            Pager(PagingConfig(pageSize = 10, enablePlaceholders = true)) {
                UserPagingSource(query, token, 1, null)
            }.flow.catch { e -> _userState.value = BaseResponse.Error(e.message) }
                .collectLatest { pagingData ->
                    _userState.value = BaseResponse.Success(pagingData)
                }
        }
    }

    fun getFollowerUser(token: String?, userId: Int) {
        _userState.value = BaseResponse.Loading()

        viewModelScope.launch {
            Pager(PagingConfig(pageSize = 10, enablePlaceholders = true)) {
                UserPagingSource(null, token, 2, userId)
            }.flow.catch { e -> _userState.value = BaseResponse.Error(e.message) }
                .collectLatest { pagingData ->
                    _userState.value = BaseResponse.Success(pagingData)
                }
        }
    }

    fun getFollowingUser(token: String?, userId: Int) {
        _userState.value = BaseResponse.Loading()

        viewModelScope.launch {
            Pager(PagingConfig(pageSize = 10, enablePlaceholders = true)) {
                UserPagingSource(null, token, 3, userId)
            }.flow.catch { e -> _userState.value = BaseResponse.Error(e.message) }
                .collectLatest { pagingData ->
                    _userState.value = BaseResponse.Success(pagingData)
                }
        }
    }

    fun follow(token: String?, userId: Int) {
        viewModelScope.launch {
            try {
                val response = userRepo.follow(token, userId)
                if (response.isSuccessful) {
                    Log.d("LOG_TAG_MAIN", response.body()!!.toString())
                    _followStatus.value = Pair(userId, response.body()!!.result.user.isFollowing)
                }
            } catch (e: Exception) {
                Log.d("LOG_TAG_MAIN", e.message.toString())
            }
        }
    }
}