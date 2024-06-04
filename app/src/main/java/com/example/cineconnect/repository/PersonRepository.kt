package com.example.cineconnect.repository

import com.example.cineconnect.model.PeopleListResponse
import com.example.cineconnect.model.Person
import com.example.cineconnect.network.API
import retrofit2.Response

class PersonRepository {
    suspend fun getPerson(id: Int): Response<Person> {
        return API.apiService.getPerson(id.toString())
    }

    suspend fun getSearchPerson(page: Int, query: String): Response<PeopleListResponse> {
        return API.apiService.getSearchPerson(page, query)
    }
}