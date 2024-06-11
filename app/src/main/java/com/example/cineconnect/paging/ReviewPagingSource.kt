package com.example.cineconnect.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cineconnect.model.ReviewList
import com.example.cineconnect.network.API
import retrofit2.HttpException
import java.io.IOException

class ReviewPagingSource(
    private val movie: Int?,
    private val type: Int,
    private val query: String?,
    private val userId: Int?,
    private val token: String?
) : PagingSource<Int, ReviewList>() {
    override fun getRefreshKey(state: PagingState<Int, ReviewList>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ReviewList> {
        val page = params.key ?: 1
        Log.d("LOG_TAG_MAIN", "type: $type")
        when (type) {
            1 -> {
                return try {
                    val response = API.apiService.getReviewList(page, movie!!)
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

            2 -> {
                return try {
                    val response = API.apiService.getSearchReviewList(page, query!!)
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

            3 -> {
                return try {
                    val response = API.apiService.getReviewListByUser(page, userId!!)
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

            4 -> {
                return try {
                    val response = API.apiService.getReviewNewFeed(page, token!!)
                    if (response.isSuccessful) {
                        val reviewListResponse = response.body()
                        Log.d("LOG_TAG_MAIN", "load: ${response.body()}")
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

            else -> {
                return try {
                    val response = API.apiService.getReviewRecommend(page, token!!)
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

    }
}