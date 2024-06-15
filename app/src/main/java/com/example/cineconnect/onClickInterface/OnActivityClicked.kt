package com.example.cineconnect.onClickInterface

interface OnActivityClicked {
    fun onReviewClicked(position: Int, review: Int)
    fun onMovieClicked(position: Int, movie: Int)
    fun onUserClicked(position: Int, user: Int)
}