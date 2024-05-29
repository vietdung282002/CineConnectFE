package com.example.cineconnect.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cineconnect.model.Person
import com.example.cineconnect.network.BaseResponse
import com.example.cineconnect.repository.PersonRepository
import kotlinx.coroutines.launch

class PersonViewModel: ViewModel() {
    private val personRepository = PersonRepository()
    val personResult: MutableLiveData<BaseResponse<Person>> = MutableLiveData()

    fun getPerson(id: Int) {
        personResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try{
                val response = personRepository.getPerson(id)

                if(response.code() == 200){
                    personResult.value = BaseResponse.Success(response.body()!!)
                }else{
                    personResult.value = BaseResponse.Error(response.message())
                }
            }
            catch (e: Exception){
                personResult.value = BaseResponse.Error(e.message.toString())
            }
        }
    }

}