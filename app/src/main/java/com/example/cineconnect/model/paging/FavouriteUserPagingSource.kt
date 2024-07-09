package com.example.cineconnect.model.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cineconnect.model.entities.FavouriteList
import com.example.cineconnect.model.network.API
import retrofit2.HttpException
import java.io.IOException

class FavouriteUserPagingSource(private val movie: Int) : PagingSource<Int, FavouriteList>() {
    override fun getRefreshKey(state: PagingState<Int, FavouriteList>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FavouriteList> {
        val page = params.key ?: 1
        return try {
            val response = API.apiService.getListUserLikeMovie(page, movie)
            if (response.isSuccessful) {
                val userListResponse = response.body()
                if (userListResponse != null) {
                    LoadResult.Page(
                        data = userListResponse.favouriteLists,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (page == userListResponse.totalPages) null else page + 1
                    )
                } else {
                    LoadResult.Error(IOException("Response body is null"))
                }
            } else {
                LoadResult.Error(HttpException(response))
            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}