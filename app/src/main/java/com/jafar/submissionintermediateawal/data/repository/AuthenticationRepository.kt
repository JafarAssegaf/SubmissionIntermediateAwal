package com.jafar.submissionintermediateawal.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.jafar.submissionintermediateawal.data.remote.response.ErrorResponse
import com.jafar.submissionintermediateawal.data.remote.retrofit.ApiService
import com.jafar.submissionintermediateawal.utils.preferences.UserPreferences
import com.jafar.submissionintermediateawal.utils.result.Result
import retrofit2.HttpException

class AuthenticationRepository private constructor(
    private val apiService: ApiService,
    private val preferences: UserPreferences
){

    private val resultRegister = MutableLiveData<Result<String?>>()
    private val resultLogin = MutableLiveData<Result<String?>>()

    suspend fun registerUser(name: String, email: String, password: String): LiveData<Result<String?>> {
        try {
            resultRegister.value = Result.Loading
            val response = apiService.register(name, email, password)
            val messageSuccess = response.message
            resultRegister.value = Result.Success(messageSuccess)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            resultRegister.value = Result.Error(errorMessage.toString())
        }
        return resultRegister
    }

    suspend fun loginUser(email: String, password: String): LiveData<Result<String?>> {
        try {
            resultLogin.value = Result.Loading
            val response = apiService.login(email, password)
            val messageSuccess = response.message

            val userTokenLogin = response.loginResult?.token
            if (userTokenLogin != null) {
                preferences.saveUserToken(userTokenLogin)
            }
            resultLogin.value = Result.Success(messageSuccess)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            resultLogin.value = Result.Error(errorMessage.toString())
        }
        return resultLogin
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthenticationRepository? = null

        fun getInstance(apiService: ApiService, preferences: UserPreferences): AuthenticationRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AuthenticationRepository(
                    apiService,
                    preferences
                )
            }
        }
    }
}