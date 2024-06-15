package com.example.cineconnect.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.azure.storage.blob.BlobClientBuilder
import com.example.cineconnect.model.Activity
import com.example.cineconnect.model.ConFirmPasscode
import com.example.cineconnect.model.CustomResponse
import com.example.cineconnect.model.FavouriteList
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
import com.example.cineconnect.model.UserList
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.paging.ActivityPagingSource
import com.example.cineconnect.paging.FavouriteUserPagingSource
import com.example.cineconnect.paging.UserPagingSource
import com.example.cineconnect.repository.UserRepository
import com.example.cineconnect.utils.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class UserViewModel : ViewModel() {

    private val userRepo = UserRepository()
    val loginResult: MutableLiveData<BaseResponse<LoginResponse>> = MutableLiveData()
    val registerResult: MutableLiveData<BaseResponse<RegisterResponse>> = MutableLiveData()
    val logoutResult: MutableLiveData<BaseResponse<CustomResponse>> = MutableLiveData()
    val resetPasswordRequestResult: MutableLiveData<BaseResponse<CustomResponse>> =
        MutableLiveData()
    val resetPasswordResult: MutableLiveData<BaseResponse<CustomResponse>> = MutableLiveData()
    val confirmPasscodeResult: MutableLiveData<BaseResponse<CustomResponse>> = MutableLiveData()
    val userResult: MutableLiveData<BaseResponse<User>> = MutableLiveData()
    val updateUserResult: MutableLiveData<BaseResponse<UpdateUser>> = MutableLiveData()
    val updatePasswordResult: MutableLiveData<BaseResponse<UpdateResponse>> = MutableLiveData()

    val userEmail = MutableLiveData<String>()

    private val _userFavouriteState =
        MutableStateFlow<BaseResponse<PagingData<FavouriteList>>>(BaseResponse.Loading())
    val userFavouriteState: StateFlow<BaseResponse<PagingData<FavouriteList>>> = _userFavouriteState

    private val _userState =
        MutableStateFlow<BaseResponse<PagingData<UserList>>>(BaseResponse.Loading())
    val userState: StateFlow<BaseResponse<PagingData<UserList>>> = _userState

    private val _followStatus = MutableLiveData<Pair<Int, Boolean?>>()
    val followStatus: LiveData<Pair<Int, Boolean?>> = _followStatus

    private val _activityState =
        MutableStateFlow<BaseResponse<PagingData<Activity>>>(BaseResponse.Loading())
    val activityState: StateFlow<BaseResponse<PagingData<Activity>>> = _activityState

    val username = MutableLiveData<String>()
    val signInAS = MutableLiveData<String>()
    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val bio = MutableLiveData<String>()
    val profilePicture = MutableLiveData<String>()
    val currentPassword = MutableLiveData<String>()
    val newPassword = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()
    private val gender = MutableLiveData<String>()

    fun login(usernameOrEmail: String, password: String) {
        loginResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(
                    password = password, usernameOrEmail = usernameOrEmail
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
                    username = username, email = email, password = password
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

    fun logout(token: String) {
        logoutResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = userRepo.logout(token)
                if (response.isSuccessful) {
                    logoutResult.value = BaseResponse.Success(response.body())
                } else {
                    logoutResult.value = BaseResponse.Error(response.message())
                }
            } catch (e: Exception) {
                logoutResult.value = BaseResponse.Error(e.message)
            }
        }
    }

    fun resetPasswordRequest() {
        resetPasswordRequestResult.value = BaseResponse.Loading()
        if (Utils.isValidEmail(userEmail.value.toString())) {
            viewModelScope.launch {
                try {
                    val resetPasswordRequest = ResetPasswordRequest(email = userEmail.value!!)
                    val response =
                        userRepo.resetPasswordRequest(resetPasswordRequest = resetPasswordRequest)
                    if (response.isSuccessful) {
                        resetPasswordRequestResult.value = BaseResponse.Success(response.body())
                    } else {
                        resetPasswordRequestResult.value =
                            BaseResponse.Error(response.body()?.message)
                    }
                } catch (e: Exception) {
                    resetPasswordRequestResult.value = BaseResponse.Error(e.message)
                }
            }

        } else {
            resetPasswordRequestResult.value = BaseResponse.Error("Invalid Email")
        }
    }

    fun confirmPasscode(passcode: String) {
        confirmPasscodeResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val conFirmPasscode = ConFirmPasscode(userEmail.value!!, passcode)

                val response = userRepo.confirmPasscode(conFirmPasscode)
                if (response.isSuccessful) {
                    confirmPasscodeResult.value = BaseResponse.Success(response.body())
                } else {
                    confirmPasscodeResult.value = BaseResponse.Error(response.body()?.message)
                }
            } catch (e: Exception) {
                confirmPasscodeResult.value = BaseResponse.Error(e.message)
            }
        }
    }

    fun resetPassword(password: String) {
        resetPasswordResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val resetPassword = ResetPassword(email = userEmail.value!!, password = password)
                val response = userRepo.resetPassword(resetPassword)
                if (response.isSuccessful) {
                    resetPasswordResult.value = BaseResponse.Success(response.body())
                } else {
                    resetPasswordResult.value = BaseResponse.Error(response.body()?.message)
                }
            } catch (e: Exception) {
                resetPasswordResult.value = BaseResponse.Error(e.message)
            }
        }

    }

    fun getUser(token: String?, userId: Int) {
        userResult.value = BaseResponse.Loading()

        viewModelScope.launch {
            try {
                val response = userRepo.getUser(token, userId)
                _followStatus.value = Pair(userId, response.body()!!.isFollowing)
                if (response.isSuccessful) {
                    userResult.value = BaseResponse.Success(response.body())
                    username.value = response.body()!!.username
                    signInAS.value = "Signed in as " + response.body()!!.username
                    firstName.value = response.body()!!.firstName
                    lastName.value = response.body()!!.lastName
                    email.value = response.body()!!.email
                    bio.value = response.body()!!.bio
                    profilePicture.value = response.body()!!.profilePic
                    gender.value = response.body()!!.gender
                } else {
                    userResult.value = BaseResponse.Error(response.message())
                }
            } catch (e: Exception) {
                userResult.value = BaseResponse.Error(e.message)
            }
        }
    }

    fun getFavourite(movieId: Int) {
        _userFavouriteState.value = BaseResponse.Loading()
        viewModelScope.launch {
            Pager(PagingConfig(pageSize = 10, enablePlaceholders = true)) {
                FavouriteUserPagingSource(movieId)
            }.flow.catch { e -> _userFavouriteState.value = BaseResponse.Error(e.message) }
                .collectLatest { pagingData ->
                    _userFavouriteState.value = BaseResponse.Success(pagingData)
                }

        }
    }

    fun getSearchUser(token: String?, query: String) {
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
                    _followStatus.value = Pair(userId, response.body()!!.result.user.isFollowing)
                }
            } catch (_: Exception) {
            }
        }
    }

    fun updateUser(token: String, userId: Int) {
        updateUserResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val updateUser = UpdateUser(
                    firstName = firstName.value,
                    lastName = lastName.value,
                    email = email.value,
                    bio = bio.value,
                    gender = gender.value,
                    profilePic = profilePicture.value,
                    username = username.value
                )
                val response = userRepo.updateUserProfile(
                    token = token, userId = userId, updateUser = updateUser
                )
                Log.d("LOG_TAG_MAIN", response.toString())
                if (response.isSuccessful) {
                    updateUserResult.value = BaseResponse.Success(response.body())
                } else {
                    updateUserResult.value = BaseResponse.Error(response.message())
                }
            } catch (e: Exception) {
                updateUserResult.value = BaseResponse.Error(e.message)
            }
        }
    }


    fun changePassword(token: String) {
        updatePasswordResult.value = BaseResponse.Loading()
        Log.d(
            "LOG_TAG_MAIN",
            "${currentPassword.value.toString()} + ${newPassword.value.toString()} + ${confirmPassword.value.toString()}"
        )
        Log.d("LOG_TAG_MAIN", "${confirmPassword.value == newPassword.value}")

        if (confirmPassword.value != newPassword.value) {
            updatePasswordResult.value = BaseResponse.Error("confirm Password not match")
        } else {
            viewModelScope.launch {
                try {
                    val updatePassword = UpdatePassword(
                        currentPassword = currentPassword.value, newPassword = newPassword.value
                    )
                    val response = userRepo.updatePassword(
                        token = token, updatePassword = updatePassword
                    )
                    Log.d("LOG_TAG_MAIN", response.toString())
                    if (response.isSuccessful) {
                        updatePasswordResult.value = BaseResponse.Success(response.body())
                    } else {
                        updatePasswordResult.value = BaseResponse.Error(response.message())
                    }
                } catch (e: Exception) {
                    updatePasswordResult.value = BaseResponse.Error(e.message)
                }
            }

        }
    }

    fun getActivityList(userId: Int) {
        _activityState.value = BaseResponse.Loading()
        viewModelScope.launch {
            Pager(PagingConfig(pageSize = 12, enablePlaceholders = true)) {
                ActivityPagingSource(userId)
            }.flow.catch { e -> _activityState.value = BaseResponse.Error(e.message) }
                .collectLatest { pagingData ->
                    _activityState.value = BaseResponse.Success(pagingData)
                }
        }
    }


    fun uploadImage(bitmap: Bitmap) {

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val imageData = outputStream.toByteArray()


        val blobClient = BlobClientBuilder().connectionString("").containerName("user-profile")
            .blobName("${System.currentTimeMillis()}.jpg").buildClient()


        val inputStream = ByteArrayInputStream(imageData)
        try {
            blobClient.upload(inputStream, imageData.size.toLong(), true)
        } catch (_: Exception) {
        }
        profilePicture.value = "/" + blobClient.blobUrl.toString().substringAfterLast("/")
    }


}