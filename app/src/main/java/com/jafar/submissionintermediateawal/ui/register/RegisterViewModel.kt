package com.jafar.submissionintermediateawal.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.jafar.submissionintermediateawal.data.remote.response.ErrorResponse
import com.jafar.submissionintermediateawal.data.repository.AuthenticationRepository
import com.jafar.submissionintermediateawal.utils.result.Result
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private val _registerState = MutableLiveData<Result<String?>>()
    val registerState: LiveData<Result<String?>>
        get() = _registerState

    fun finalRegisterUser(
        name: String, email: String, password: String
    ) = viewModelScope.launch {
        _registerState.value = Result.Loading

        try {
            val registerResponse = authenticationRepository.finalRegisterUser(name, email, password)
            val messageSuccess = registerResponse.message
            _registerState.value = Result.Success(messageSuccess)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _registerState.value = Result.Error(errorMessage.toString())
        }
    }

//    suspend fun registerUser(
//        name: String, email: String, password: String
//    ) = authenticationRepository.registerUser(name, email, password)
}