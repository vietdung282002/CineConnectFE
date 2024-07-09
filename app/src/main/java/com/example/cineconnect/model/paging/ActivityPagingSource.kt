package com.example.cineconnect.model.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cineconnect.model.entities.Activity
import com.example.cineconnect.model.network.API
import retrofit2.HttpException
import java.io.IOException

class ActivityPagingSource(private val userId: Int) : PagingSource<Int, Activity>() {
    override fun getRefreshKey(state: PagingState<Int, Activity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Activity> {
        val page = params.key ?: 1
        return try {
            val response = API.apiService.getActivity(page, userId)
            if (response.isSuccessful) {
                val movieListResponse = response.body()
                if (movieListResponse != null) {
                    LoadResult.Page(
                        data = movieListResponse.results,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (page == movieListResponse.totalPages) null else page + 1
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