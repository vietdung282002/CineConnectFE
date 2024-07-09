package com.example.cineconnect.model.repository

import com.example.cineconnect.model.entities.PeopleListResponse
import com.example.cineconnect.model.entities.Person
import com.example.cineconnect.model.network.API
import retrofit2.Response

class PersonRepository {
    suspend fun getPerson(id: Int): Response<Person> {
        return API.apiService.getPerson(id.toString())
    }

    suspend fun getSearchPerson(page: Int, query: String): Response<PeopleListResponse> {
        return API.apiService.getSearchPerson(page, query)
    }
}