package com.example.cineconnect.model

import com.google.gson.annotations.SerializedName

data class FollowResponse(
    @SerializedName("status") val status: String,
    @SerializedName("result") val result: FollowResult
)

data class FollowResult(
    @SerializedName("user") val user: UserList,
)

data class FollowListResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val userLists: List<UserList>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("current_page") val currentPage: Int
)


