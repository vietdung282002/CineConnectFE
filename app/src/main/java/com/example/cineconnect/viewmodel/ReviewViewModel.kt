package com.example.cineconnect.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.cineconnect.model.Comment
import com.example.cineconnect.model.Review
import com.example.cineconnect.model.ReviewList
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.paging.CommentPagingSource
import com.example.cineconnect.paging.ReviewPagingSource
import com.example.cineconnect.repository.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ReviewViewModel : ViewModel() {
    private val reviewRepository = ReviewRepository()
    val reviewResult: MutableLiveData<BaseResponse<Review>> = MutableLiveData()
    private val _commentState =
        MutableStateFlow<BaseResponse<PagingData<Comment>>>(BaseResponse.Loading())
    val commentState: StateFlow<BaseResponse<PagingData<Comment>>> = _commentState

    private val _reviewState =
        MutableStateFlow<BaseResponse<PagingData<ReviewList>>>(BaseResponse.Loading())
    val reviewState: StateFlow<BaseResponse<PagingData<ReviewList>>> = _reviewState

    val likeState: MutableLiveData<Boolean?> = MutableLiveData()
    val numberOfLike: MutableLiveData<Int> = MutableLiveData()

    fun getReview(token: String?, id: Int) {
        reviewResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = reviewRepository.getReviews(token, id)

                if (response.isSuccessful) {
                    reviewResult.value = BaseResponse.Success(response.body())
                    likeState.value = response.body()?.isLiked
                    numberOfLike.value = response.body()?.likesCount
                } else {
                    reviewResult.value = BaseResponse.Error(response.message())
                }
            } catch (e: Exception) {
                reviewResult.value = BaseResponse.Error(e.message)
            }
        }
    }

    fun getReviewList(movieId: Int) {
        _reviewState.value = BaseResponse.Loading()
        viewModelScope.launch {
            Pager(PagingConfig(pageSize = 12, enablePlaceholders = true)) {
                ReviewPagingSource(movieId, 1, null, null)
            }.flow.catch { e -> _reviewState.value = BaseResponse.Error(e.message) }
                .collectLatest { pagingData ->
                    _reviewState.value = BaseResponse.Success(pagingData)
                }
        }
    }

    fun getReviewListByUser(userId: Int) {
        _reviewState.value = BaseResponse.Loading()
        viewModelScope.launch {
            Pager(PagingConfig(pageSize = 12, enablePlaceholders = true)) {
                ReviewPagingSource(null, 3, null, userId)
            }.flow.catch { e -> _reviewState.value = BaseResponse.Error(e.message) }
                .collectLatest { pagingData ->
                    _reviewState.value = BaseResponse.Success(pagingData)
                }
        }
    }

    fun getSearchReviewList(query: String) {
        _reviewState.value = BaseResponse.Loading()
        viewModelScope.launch {
            Pager(PagingConfig(pageSize = 12, enablePlaceholders = true)) {
                ReviewPagingSource(null, 2, query, null)
            }.flow.catch { e -> _reviewState.value = BaseResponse.Error(e.message) }
                .collectLatest { pagingData ->
                    _reviewState.value = BaseResponse.Success(pagingData)
                }
        }
    }

    fun getReviewCommentList(reviewId: Int) {
        _commentState.value = BaseResponse.Loading()
        viewModelScope.launch {
            Pager(PagingConfig(pageSize = 12, enablePlaceholders = true)) {
                CommentPagingSource(reviewId)
            }.flow.catch { e -> _commentState.value = BaseResponse.Error(e.message) }
                .collectLatest { pagingData ->
                    _commentState.value = BaseResponse.Success(pagingData)
                }
        }
    }

    fun like(token: String, id: Int) {
        viewModelScope.launch {
            val response = reviewRepository.like(token, id)
            if (response.isSuccessful) {
                likeState.value = response.body()?.result?.like
                numberOfLike.value = response.body()?.result?.numberOfLike
            }
        }
    }

}