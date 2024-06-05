package com.example.cineconnect.repository

import com.example.cineconnect.model.Review
import com.example.cineconnect.network.API
import retrofit2.Response

class ReviewRepository {
    suspend fun getReviews(reviewId: Int): Response<Review> {
        return API.apiService.getReviewDetail(reviewId)
    }
}