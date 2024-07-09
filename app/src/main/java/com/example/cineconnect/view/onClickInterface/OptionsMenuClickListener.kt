package com.example.cineconnect.view.onClickInterface

interface OptionsMenuClickListener {
    fun onDeleteClicked(position: Int, commentId: Int)

    fun onEditClicked(position: Int, commentId: Int)
}