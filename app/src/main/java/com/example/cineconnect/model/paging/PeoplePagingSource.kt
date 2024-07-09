package com.example.cineconnect.model.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.cineconnect.model.entities.Person
import com.example.cineconnect.model.network.API
import retrofit2.HttpException
import java.io.IOException

class PeoplePagingSource(private val query: String) : PagingSource<Int, Person>() {
    override fun getRefreshKey(state: PagingState<Int, Person>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Person> {
        val page = params.key ?: 1
        return try {
            val response = API.apiService.getSearchPerson(page, query)
            if (response.isSuccessful) {
                val peopleLists = response.body()
                if (peopleLists != null) {
                    LoadResult.Page(
                        data = peopleLists.personLists,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (page == peopleLists.totalPages) null else page + 1
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