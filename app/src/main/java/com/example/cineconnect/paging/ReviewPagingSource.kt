package com.example.cineconnect.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cineconnect.model.ReviewList
import com.example.cineconnect.network.API
import retrofit2.HttpException
import java.io.IOException

class ReviewPagingSource(private val movie: Int) : PagingSource<Int, ReviewList>() {
    override fun getRefreshKey(state: PagingState<Int, ReviewList>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ReviewList> {
        val page = params.key ?: 1
        return try {
            val response = API.apiService.getReviewList(page, movie)
            if (response.isSuccessful) {
                val reviewListResponse = response.body()
                if (reviewListResponse != null) {
                    LoadResult.Page(
                        data = reviewListResponse.reviewLists,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (page == reviewListResponse.totalPages) null else page + 1
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