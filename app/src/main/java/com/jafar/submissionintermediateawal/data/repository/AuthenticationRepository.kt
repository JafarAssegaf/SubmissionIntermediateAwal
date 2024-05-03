package com.jafar.submissionintermediateawal.data.repository

import com.jafar.submissionintermediateawal.data.remote.retrofit.ApiService
import com.jafar.submissionintermediateawal.utils.preferences.UserPreferences
import kotlinx.coroutines.flow.Flow

class AuthenticationRepository private constructor(
    private val apiService: ApiService,
    private val preferences: UserPreferences
){

    suspend fun finalRegisterUser(name: String, email: String, password: String) =
        apiService.register(name, email, password)

    suspend fun finalLoginUser(name: String, email: String) =
        apiService.login(name, email)

    suspend fun setUserToken(token: String) {
        preferences.saveUserToken(token)
    }

    fun getUserToken(): Flow<String> {
        return preferences.getUserToken()
    }

    suspend fun clearToken() {
        preferences.clearToken()
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthenticationRepository? = null

        fun getInstance(apiService: ApiService, preferences: UserPreferences): AuthenticationRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AuthenticationRepository(
                    apiService,
                    preferences
                ).also {
                    INSTANCE = it
                }
            }
        }
    }
}