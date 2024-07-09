package com.example.cineconnect.model.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cineconnect.model.entities.UserList
import com.example.cineconnect.model.network.API
import retrofit2.HttpException
import java.io.IOException

class UserPagingSource(
    private val query: String?,
    private val token: String?,
    private val type: Int,
    private val userId: Int?
) : PagingSource<Int, UserList>() {
    override fun getRefreshKey(state: PagingState<Int, UserList>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserList> {
        val page = params.key ?: 1
        when (type) {
            1 -> {
                return try {
                    val response = query?.let { API.apiService.getSearchUser(token, page, it) }
                    if (response?.isSuccessful == true) {
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
                        LoadResult.Error(HttpException(response!!))
                    }
                } catch (e: IOException) {
                    LoadResult.Error(e)
                } catch (e: HttpException) {
                    LoadResult.Error(e)
                }
            }

            2 -> {
                return try {
                    val response = userId?.let { API.apiService.getFollowerUser(token, page, it) }
                    if (response?.isSuccessful == true) {
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
                        LoadResult.Error(HttpException(response!!))
                    }
                } catch (e: IOException) {
                    LoadResult.Error(e)
                } catch (e: HttpException) {
                    LoadResult.Error(e)
                }
            }

            else -> {
                return try {
                    val response = userId?.let { API.apiService.getFollowingUser(token, page, it) }
                    if (response?.isSuccessful == true) {
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
                        LoadResult.Error(HttpException(response!!))
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