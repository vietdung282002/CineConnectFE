package com.example.cineconnect.repository

import com.example.cineconnect.model.LikeResponse
import com.example.cineconnect.model.Review
import com.example.cineconnect.network.API
import retrofit2.Response

class ReviewRepository {
    suspend fun getReviews(token: String?, reviewId: Int): Response<Review> {
        return API.apiService.getReviewDetail(token, reviewId)
    }

    suspend fun like(token: String, reviewId: Int): Response<LikeResponse> {
        return API.apiService.like(token, reviewId)
    }
}