package com.example.cineconnect.onClickInterface

import com.example.cineconnect.model.Movie

interface OnMovieClicked {
    fun getOnMovieClicked(position: Int, movieId: Int)
}