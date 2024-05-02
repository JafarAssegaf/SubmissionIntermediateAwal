package com.jafar.submissionintermediateawal.di

import android.content.Context
import com.jafar.submissionintermediateawal.data.remote.retrofit.ApiConfig
import com.jafar.submissionintermediateawal.data.repository.AuthenticationRepository
import com.jafar.submissionintermediateawal.data.repository.StoryRepository
import com.jafar.submissionintermediateawal.utils.preferences.UserPreferences
import com.jafar.submissionintermediateawal.utils.preferences.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun provideAuthRepository(context: Context): AuthenticationRepository {
        val preferences = UserPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiServiceWithoutToken()
        return AuthenticationRepository.getInstance(apiService, preferences)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val preferences = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { preferences.getUserToken().first() }
        val apiService = ApiConfig.getApiServiceWithToken(user)
        return StoryRepository.getInstance(apiService)
    }

}