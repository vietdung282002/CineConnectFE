package com.example.cineconnect.repository

import com.example.cineconnect.model.Comment
import com.example.cineconnect.model.CommentRequest
import com.example.cineconnect.model.LikeResponse
import com.example.cineconnect.model.Review
import com.example.cineconnect.model.ReviewRequest
import com.example.cineconnect.model.UpdateResponse
import com.example.cineconnect.network.API
import retrofit2.Response

class ReviewRepository {
    suspend fun getReviews(token: String?, reviewId: Int): Response<Review> {
        return API.apiService.getReviewDetail(token, reviewId)
    }

    suspend fun getComment(commentId: Int): Response<Comment> {
        return API.apiService.getComment(commentId)
    }

    suspend fun like(token: String, reviewId: Int): Response<LikeResponse> {
        return API.apiService.like(token, reviewId)
    }

    suspend fun comment(token: String, commentRequest: CommentRequest): Response<UpdateResponse> {
        return API.apiService.comment(token, commentRequest)
    }

    suspend fun editReview(
        token: String,
        reviewId: Int,
        reviewRequest: ReviewRequest
    ): Response<ReviewRequest> {
        return API.apiService.editReview(token, reviewRequest, reviewId)
    }

    suspend fun editComment(
        token: String,
        commentRequest: CommentRequest,
        commentId: Int
    ): Response<CommentRequest> {
        return API.apiService.editComment(token, commentRequest, commentId)
    }

    suspend fun deleteReview(token: String, reviewId: Int): Response<Unit> {
        return API.apiService.deleteReview(token, reviewId)
    }

    suspend fun deleteComment(token: String, commentId: Int): Response<Unit> {
        return API.apiService.deleteComment(token, commentId)
    }


}