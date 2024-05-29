package com.example.cineconnect.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cineconnect.model.Movie
import com.example.cineconnect.model.MovieListResponse
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.repository.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel: ViewModel() {
    private val movieRepository = MovieRepository()
    val movieListResult: MutableLiveData<BaseResponse<MovieListResponse>> = MutableLiveData()
    val movieResult: MutableLiveData<BaseResponse<Movie>> = MutableLiveData()

    fun getMovieList() {
        movieListResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = movieRepository.movieList()

                if(response.code() == 200){
                    movieListResult.value = BaseResponse.Success(response.body())
                }
                else{
                    movieListResult.value = BaseResponse.Error(response.message())
                }
            }
            catch (e: Exception){
                movieListResult.value = BaseResponse.Error(e.message)
            }
        }
    }

    fun getMovie(id: Int){
        movieResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = movieRepository.getMovie(id)

                if(response.code() == 200){
                    movieResult.value = BaseResponse.Success(response.body())
                }
                else{
                    movieResult.value = BaseResponse.Error(response.message())
                }
            }
            catch (e: Exception){
                movieResult.value = BaseResponse.Error(e.message)
            }
        }
    }

    fun getMovieListByGenre(genreId: Int){
        movieListResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = movieRepository.getMovieByGenre(genreId)

                if(response.code() == 200){
                    movieListResult.value = BaseResponse.Success(response.body())
                }
                else{
                    movieListResult.value = BaseResponse.Error(response.message())
                }
            }
            catch (e: Exception){
                movieListResult.value = BaseResponse.Error(e.message)
            }
        }
    }
}