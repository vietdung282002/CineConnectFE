package com.example.cineconnect.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cineconnect.model.FavouriteList
import com.example.cineconnect.model.UserList
import com.example.cineconnect.network.API
import retrofit2.HttpException
import java.io.IOException

class UserPagingSource(private val query: String,private val token: String?) : PagingSource<Int, UserList>() {
    override fun getRefreshKey(state: PagingState<Int, UserList>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserList> {
        val page = params.key ?: 1
        return try {
            val response = API.apiService.getSearchUser(token, page, query)
            if (response.isSuccessful) {
                val userListResponse = response.body()
                if (userListResponse != null) {
                    LoadResult.Page(
                        data = userListResponse.userLists,
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