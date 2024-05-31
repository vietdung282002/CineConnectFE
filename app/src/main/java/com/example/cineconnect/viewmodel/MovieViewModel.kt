package com.example.cineconnect.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.cineconnect.model.Movie
import com.example.cineconnect.model.MovieList
import com.example.cineconnect.model.MovieListResponse
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.paging.MoviePagingSource
import com.example.cineconnect.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    private val movieRepository = MovieRepository()
    val movieListResult: MutableLiveData<BaseResponse<MovieListResponse>> = MutableLiveData()
    val movieResult: MutableLiveData<BaseResponse<Movie>> = MutableLiveData()
    val searchQuery = MutableLiveData<String>()
    private val _moviesState = MutableStateFlow<BaseResponse<PagingData<MovieList>>>(BaseResponse.Loading())
    val moviesState: StateFlow<BaseResponse<PagingData<MovieList>>> = _moviesState

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

    fun getMovie(id: Int) {
        movieResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = movieRepository.getMovie(id)

                if (response.isSuccessful) {
                    movieResult.value = BaseResponse.Success(response.body())
                } else {
                    movieResult.value = BaseResponse.Error(response.message())
                }
            } catch (e: Exception) {
                movieResult.value = BaseResponse.Error(e.message)
            }
        }
    }

    fun getMovieListByGenre(genreId: Int) {
        movieListResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = movieRepository.getMovieByGenre(genreId)

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
            Pager(PagingConfig(pageSize = 10,enablePlaceholders = true)) {
                MoviePagingSource(query)
            }.flow
                .catch { e -> _moviesState.value = BaseResponse.Error(e.message) }
                .collectLatest { pagingData -> _moviesState.value = BaseResponse.Success(pagingData) }
        }
    }
}