package com.example.cineconnect.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.cineconnect.model.entities.Comment
import com.example.cineconnect.model.entities.CommentRequest
import com.example.cineconnect.model.entities.CustomResponse
import com.example.cineconnect.model.entities.Review
import com.example.cineconnect.model.entities.ReviewList
import com.example.cineconnect.model.entities.ReviewRequest
import com.example.cineconnect.model.network.BaseResponse
import com.example.cineconnect.model.paging.CommentPagingSource
import com.example.cineconnect.model.paging.ReviewPagingSource
import com.example.cineconnect.model.repository.ReviewRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ReviewViewModel : ViewModel() {
    private val reviewRepository = ReviewRepository()
    val reviewResult: MutableLiveData<BaseResponse<Review>> = MutableLiveData()
    var editReviewResult: MutableLiveData<BaseResponse<Unit>> = MutableLiveData()
    private val getCommentResult: MutableLiveData<BaseResponse<Comment>> = MutableLiveData()
    val editCommentResult: MutableLiveData<BaseResponse<Unit>> = MutableLiveData()

    private val _commentState =
        MutableStateFlow<BaseResponse<PagingData<Comment>>>(BaseResponse.Loading())
    val commentState: StateFlow<BaseResponse<PagingData<Comment>>> = _commentState

    private val _reviewState =
        MutableStateFlow<BaseResponse<PagingData<ReviewList>>>(BaseResponse.Loading())
    val reviewState: StateFlow<BaseResponse<PagingData<ReviewList>>> = _reviewState

    val likeState: MutableLiveData<Boolean?> = MutableLiveData()
    val numberOfLike: MutableLiveData<Int> = MutableLiveData()
    val comment: MutableLiveData<String> = MutableLiveData()
    val commentResult: MutableLiveData<BaseResponse<Unit>> = MutableLiveData()

    val reviewContent: MutableLiveData<String> = MutableLiveData()
    val editContent: MutableLiveData<String> = reviewContent
    private val commentContent: MutableLiveData<String> = MutableLiveData()
    val editCommentContent: MutableLiveData<String> = commentContent
    val userDetail: MutableLiveData<String> = MutableLiveData()
    val movieId: MutableLiveData<Int> = MutableLiveData()
    val reviewId: MutableLiveData<Int> = MutableLiveData()

    fun getReview(token: String?, id: Int) {
        reviewResult.value = BaseResponse.Loading()
        editReviewResult = MutableLiveData()
        viewModelScope.launch {
            try {
                val response = reviewRepository.getReviews(token, id)

                if (response.isSuccessful) {
                    reviewResult.value = BaseResponse.Success(response.body())
                    likeState.value = response.body()?.isLiked
                    numberOfLike.value = response.body()?.likesCount
                    reviewContent.value = response.body()?.content
                    movieId.value = response.body()?.movie?.id
                    reviewId.value = response.body()?.id
                } else {
                    reviewResult.value = BaseResponse.Error(response.message())
                }
            } catch (e: Exception) {
                reviewResult.value = BaseResponse.Error(e.message)
            }
        }
    }

    fun getComment(id: Int) {
        getCommentResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = reviewRepository.getComment(id)

                if (response.isSuccessful) {
                    getCommentResult.value = BaseResponse.Success(response.body())
                    commentContent.value = response.body()?.comment
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
                ReviewPagingSource(movieId, 1, null, null, null)
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
                ReviewPagingSource(null, 3, null, userId, null)
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
                ReviewPagingSource(null, 2, query, null, null)
            }.flow.catch { e -> _reviewState.value = BaseResponse.Error(e.message) }
                .collectLatest { pagingData ->
                    _reviewState.value = BaseResponse.Success(pagingData)
                }
        }
    }

    fun getNewsFeedReviewList(token: String) {
        _reviewState.value = BaseResponse.Loading()
        viewModelScope.launch {
            Pager(PagingConfig(pageSize = 12, enablePlaceholders = true)) {
                ReviewPagingSource(null, 4, null, null, token)
            }.flow.catch { e -> _reviewState.value = BaseResponse.Error(e.message) }
                .collectLatest { pagingData ->
                    _reviewState.value = BaseResponse.Success(pagingData)
                }
        }
    }

    fun getRecommendReviewList(token: String) {
        _reviewState.value = BaseResponse.Loading()
        viewModelScope.launch {
            Pager(PagingConfig(pageSize = 12, enablePlaceholders = true)) {
                ReviewPagingSource(null, 5, null, null, token)
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

    fun comment(token: String, reviewId: Int) {
        viewModelScope.launch {
            try {
                val commentRequest = CommentRequest(
                    review = reviewId, comment = comment.value.toString()
                )
                val response = reviewRepository.comment(token, commentRequest)
                if (response.isSuccessful) {
                    comment.value = ""
                    commentResult.value = BaseResponse.Success(Unit)
                } else {
                    val errorBody = response.errorBody()?.string()
                    errorBody?.let {
                        val gson = Gson()
                        val errorResponse = gson.fromJson(it, CustomResponse::class.java)
                        commentResult.value = BaseResponse.Error(errorResponse.message)
                    }
                }
            } catch (e: Exception) {
                commentResult.value = BaseResponse.Error(e.message)
            }

        }
    }

    fun editReview(token: String, content: String) {
        editReviewResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val reviewRequest = ReviewRequest(
                    movie = movieId.value!!, content = content
                )
                val response = reviewRepository.editReview(token, reviewId.value!!, reviewRequest)
                if (response.isSuccessful) {
                    editReviewResult.value = BaseResponse.Success()
                } else {
                    val errorBody = response.errorBody()?.string()
                    errorBody?.let {
                        val gson = Gson()
                        val errorResponse = gson.fromJson(it, CustomResponse::class.java)
                        editReviewResult.value = BaseResponse.Error(errorResponse.message)
                    }
                }
            } catch (e: Exception) {
                editReviewResult.value = BaseResponse.Error(e.message)
            }
        }
    }

    fun editComment(token: String, content: String, commentId: Int, reviewId: Int) {
        editCommentResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            val commentRequest = CommentRequest(
                review = reviewId, comment = content
            )
            try {
                val response = reviewRepository.editComment(token, commentRequest, commentId)
                if (response.isSuccessful) {
                    editCommentResult.value = BaseResponse.Success()
                } else {
                    val errorBody = response.errorBody()?.string()
                    errorBody?.let {
                        val gson = Gson()
                        val errorResponse = gson.fromJson(it, CustomResponse::class.java)
                        editCommentResult.value = BaseResponse.Error(errorResponse.message)
                    }
                }
            } catch (e: Exception) {
                editCommentResult.value = BaseResponse.Error(e.message)
            }
        }
    }

    fun deleteReview(token: String) {
        editReviewResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = reviewRepository.deleteReview(token, reviewId.value!!)
                if (response.isSuccessful) {
                    editReviewResult.value = BaseResponse.Success()
                } else {
                    val errorBody = response.errorBody()?.string()
                    errorBody?.let {
                        val gson = Gson()
                        val errorResponse = gson.fromJson(it, CustomResponse::class.java)
                        editReviewResult.value = BaseResponse.Error(errorResponse.message)
                    }
                }
            } catch (e: Exception) {
                editReviewResult.value = BaseResponse.Error(e.message)
            }
        }
    }

    fun deleteComment(token: String, commentId: Int) {
        editCommentResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = reviewRepository.deleteComment(token, commentId)
                if (response.isSuccessful) {
                    editCommentResult.value = BaseResponse.Success()
                } else {
                    val errorBody = response.errorBody()?.string()
                    errorBody?.let {
                        val gson = Gson()
                        val errorResponse = gson.fromJson(it, CustomResponse::class.java)
                        editCommentResult.value = BaseResponse.Error(errorResponse.message)
                    }
                }
            } catch (e: Exception) {
                editCommentResult.value = BaseResponse.Error(e.message)
            }
        }
    }

}