package com.example.cineconnect.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.cineconnect.model.entities.CustomResponse
import com.example.cineconnect.model.entities.Movie
import com.example.cineconnect.model.entities.MovieList
import com.example.cineconnect.model.entities.MovieListResponse
import com.example.cineconnect.model.entities.MovieRequest
import com.example.cineconnect.model.entities.Rating
import com.example.cineconnect.model.entities.RatingRequest
import com.example.cineconnect.model.entities.Recommend
import com.example.cineconnect.model.entities.ReviewRequest
import com.example.cineconnect.model.network.BaseResponse
import com.example.cineconnect.model.paging.MoviePagingSource
import com.example.cineconnect.model.repository.MovieRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    private val movieRepository = MovieRepository()
    val movieListResult: MutableLiveData<BaseResponse<MovieListResponse>> = MutableLiveData()
    val movieResult: MutableLiveData<BaseResponse<Movie>> = MutableLiveData()
    val reviewResult: MutableLiveData<BaseResponse<Unit>> = MutableLiveData()

    val searchQuery = MutableLiveData<String>()
    private val _moviesState =
        MutableStateFlow<BaseResponse<PagingData<MovieList>>>(BaseResponse.Loading())
    val moviesState: StateFlow<BaseResponse<PagingData<MovieList>>> = _moviesState

    val rateState: MutableLiveData<BaseResponse<Rating>> = MutableLiveData()
    val watchState: MutableLiveData<BaseResponse<Boolean>> = MutableLiveData()
    val favoriteState: MutableLiveData<BaseResponse<Boolean>> = MutableLiveData()


    val movieId = MutableLiveData<Int>()
    val releaseYear = MutableLiveData<String>()
    val director = MutableLiveData<String>()
    val runtime = MutableLiveData<String>()
    val tagline = MutableLiveData<String>()
    val numberOfLikes = MutableLiveData<String>()
    val numberOfReviews = MutableLiveData<String>()
    val avr = MutableLiveData<String>()
    val overview = MutableLiveData<String>()
    val isFavorite = MutableLiveData<Boolean>()
    val isWatched = MutableLiveData<Boolean>()

    val reviewContent = MutableLiveData<String>()

    fun getMovieList(page: Int) {
        movieListResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = movieRepository.movieList(page)

                if (response.isSuccessful) {
                    movieListResult.value = BaseResponse.Success(response.body())
                } else {
                    movieListResult.value = BaseResponse.Error(response.message())
                }
            } catch (e: Exception) {
                movieListResult.value = BaseResponse.Error(e.message)
            }
        }
    }

    fun getMovie(token:String?, id: Int) {
        movieResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = movieRepository.getMovie(token,id)
                if (response.isSuccessful) {
                    movieResult.value = BaseResponse.Success(response.body())
                    rateState.value = BaseResponse.Success(response.body()?.rating)
                    movieId.value = response.body()?.id
                    releaseYear.value = response.body()?.releaseDate?.take(4)
                    director.value = response.body()?.directors?.get(0)?.name
                    runtime.value = response.body()?.runtime.toString() + " mins"
                    tagline.value = response.body()?.tagline
                    overview.value = response.body()?.overview
                    numberOfLikes.value = response.body()?.favouriteCount.toString()
                    numberOfReviews.value = response.body()?.reviewCount.toString()
                    avr.value = response.body()?.rating?.avr?.rateAvg.toString()
                    isFavorite.value = response.body()?.isFavourite
                    isWatched.value = response.body()?.isWatched
                } else {
                    movieResult.value = BaseResponse.Error(response.message())
                }
            } catch (e: Exception) {
                movieResult.value = BaseResponse.Error(e.message)
            }
        }
    }

    fun getMovieListByGenre(page: Int, genreId: Int) {
        movieListResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = movieRepository.getMovieByGenre(page, genreId)
                if (response.isSuccessful) {
                    movieListResult.value = BaseResponse.Success(response.body())
                } else {
                    movieListResult.value = BaseResponse.Error(response.message())
                }
            } catch (e: Exception) {
                movieListResult.value = BaseResponse.Error(e.message)
            }
        }
    }

    fun getUserFavoriteMovie(page: Int, userId: Int) {
        movieListResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = movieRepository.getUserFavoriteMovie(page, userId)
                if (response.isSuccessful) {
                    movieListResult.value = BaseResponse.Success(response.body())
                } else {
                    movieListResult.value = BaseResponse.Error(response.message())
                }
            } catch (e: Exception) {
                movieListResult.value = BaseResponse.Error(e.message)
            }
        }
    }

    fun getUserWatchedMovie(page: Int, userId: Int) {
        movieListResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = movieRepository.getUserWatchedMovie(page, userId)
                if (response.isSuccessful) {
                    movieListResult.value = BaseResponse.Success(response.body())
                } else {
                    movieListResult.value = BaseResponse.Error(response.message())
                }
            } catch (e: Exception) {
                movieListResult.value = BaseResponse.Error(e.message)
            }
        }
    }

    fun searchMovies(query: String) {
        _moviesState.value = BaseResponse.Loading()
        viewModelScope.launch {
            Pager(PagingConfig(pageSize = 12, enablePlaceholders = true)) {
                MoviePagingSource(query)
            }.flow
                .catch { e -> _moviesState.value = BaseResponse.Error(e.message) }
                .collectLatest { pagingData ->
                    _moviesState.value = BaseResponse.Success(pagingData)
                }
        }
    }

    fun rateMovie(token: String, rating: Float, movieId: Int) {
        viewModelScope.launch {
            try {
                val ratingRequest = RatingRequest(
                    movieId = movieId,
                    rating = rating
                )
                val response = movieRepository.rateMovie(token, ratingRequest = ratingRequest)
                if (response.isSuccessful) {
                    rateState.value = BaseResponse.Success(response.body()?.message?.rating)
                    if (rating > 4) {
                        val recommend = Recommend(movie = movieId)
                        movieRepository.addRecommend(token, recommend)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    errorBody?.let {
                        val gson = Gson()
                        val errorResponse = gson.fromJson(it, CustomResponse::class.java)
                        rateState.value = BaseResponse.Error(errorResponse.message)
                    }
                }
            } catch (e: Exception) {
                rateState.value = BaseResponse.Error(e.message)
            }
        }
    }

    fun watch(token: String) {
        viewModelScope.launch {
            try {
                val watchRequest =
                    MovieRequest(
                        movieId = movieId.value!!,
                    )
                val response = movieRepository.watch(token, movieRequest = watchRequest)
                if (response.isSuccessful) {
                    watchState.value = BaseResponse.Success(response.body()?.message?.watched)
                    isWatched.value = response.body()?.message?.watched
                } else if (response.code() == 405) {
                    rateState.value =
                        BaseResponse.Error("You can't not remove from watched because there is activity on it")
                } else {
                    val errorBody = response.errorBody()?.string()
                    errorBody?.let {
                        val gson = Gson()
                        val errorResponse = gson.fromJson(it, CustomResponse::class.java)
                        watchState.value = BaseResponse.Error(errorResponse.message)
                    }

                }
            } catch (e: Exception) {
                rateState.value = BaseResponse.Error(e.message)
            }
        }
    }

    fun like(token: String) {
        viewModelScope.launch {
            try {
                val favourite =
                    MovieRequest(
                        movieId = movieId.value!!,
                    )
                val response = movieRepository.favourite(token, movieRequest = favourite)
                if (response.isSuccessful) {
                    favoriteState.value = BaseResponse.Success(response.body()?.message?.favourite)
                    isFavorite.value = response.body()?.message?.favourite
                    if (isFavorite.value == true) {
                        val recommend = Recommend(movie = movieId.value!!)
                        val recommendResponse = movieRepository.addRecommend(token, recommend)

                    }
                } else {
                    favoriteState.value = BaseResponse.Error(response.message())
                }
            } catch (e: Exception) {
                favoriteState.value = BaseResponse.Error(e.message)
            }
        }
    }

    fun addReview(token: String) {
        viewModelScope.launch {
            try {
                val reviewRequest = ReviewRequest(
                    movie = movieId.value!!,
                    content = reviewContent.value!!
                )
                val response = movieRepository.addReview(token, reviewRequest = reviewRequest)
                if (response.isSuccessful) {
                    reviewResult.value = BaseResponse.Success()
                } else {
                    val errorBody = response.errorBody()?.string()
                    errorBody?.let {
                        val gson = Gson()
                        val errorResponse = gson.fromJson(it, CustomResponse::class.java)
                        reviewResult.value = BaseResponse.Error(errorResponse.message)
                    }
                }
            } catch (e: Exception) {
                reviewResult.value = BaseResponse.Error(e.message)
            }
        }
    }

}