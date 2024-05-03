package com.jafar.submissionintermediateawal.ui.login

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

class LoginViewModel(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private val _loginState = MutableLiveData<Result<String?>>()
    val loginState: LiveData<Result<String?>>
        get() = _loginState

    fun finalLoginUser(
        email: String, password: String
    ) = viewModelScope.launch {
        _loginState.value = Result.Loading

        try {
            val loginResponse = authenticationRepository.finalLoginUser(email, password)
            val messageSuccess = loginResponse.message
            _loginState.value = Result.Success(messageSuccess)

            val tokenLoginUser = loginResponse.loginResult?.token
            if (tokenLoginUser != null) {
                authenticationRepository.setUserToken(tokenLoginUser)
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _loginState.value = Result.Error(errorMessage.toString())
        }
    }
}