package com.example.cineconnect.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.cineconnect.model.MovieList
import com.example.cineconnect.model.PeopleListResponse
import com.example.cineconnect.model.Person
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.paging.MoviePagingSource
import com.example.cineconnect.paging.PeoplePagingSource
import com.example.cineconnect.repository.PersonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PersonViewModel : ViewModel() {
    private val personRepository = PersonRepository()
    val personResult: MutableLiveData<BaseResponse<Person>> = MutableLiveData()
    val personListResult: MutableLiveData<BaseResponse<PeopleListResponse>> = MutableLiveData()
    private val _peopleState = MutableStateFlow<BaseResponse<PagingData<Person>>>(BaseResponse.Loading())
    val peopleState: StateFlow<BaseResponse<PagingData<Person>>> = _peopleState

    fun getPerson(id: Int) {
        personResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val response = personRepository.getPerson(id)

                if (response.isSuccessful) {
                    personResult.value = BaseResponse.Success(response.body()!!)
                } else {
                    personResult.value = BaseResponse.Error(response.message())
                }
            } catch (e: Exception) {
                personResult.value = BaseResponse.Error(e.message.toString())
            }
        }
    }

    fun getSearchPerson(page:Int,query:String){
        personListResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try{
                val response = personRepository.getSearchPerson(page,query)
                if(response.isSuccessful){
                    personListResult.value = BaseResponse.Success(response.body()!!)
                }
                else{
                    personListResult.value = BaseResponse.Error(response.message())
                }

            }catch (e:Exception){
                personListResult.value = BaseResponse.Error(e.message.toString())
            }
        }
    }

    fun searchPeople(query: String) {
        _peopleState.value = BaseResponse.Loading()
        viewModelScope.launch {
            Pager(PagingConfig(pageSize = 10,enablePlaceholders = true)) {
                PeoplePagingSource(query)
            }.flow
                .catch { e -> _peopleState.value = BaseResponse.Error(e.message) }
                .collectLatest { pagingData -> _peopleState.value = BaseResponse.Success(pagingData) }
        }
    }

}