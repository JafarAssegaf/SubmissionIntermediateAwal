package com.jafar.submissionintermediateawal.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jafar.submissionintermediateawal.data.repository.AuthenticationRepository
import com.jafar.submissionintermediateawal.data.repository.StoryRepository
import com.jafar.submissionintermediateawal.di.Injection
import com.jafar.submissionintermediateawal.ui.add_story.AddStoryViewModel
import com.jafar.submissionintermediateawal.ui.detail.DetailViewModel
import com.jafar.submissionintermediateawal.ui.home.HomeViewModel
import com.jafar.submissionintermediateawal.ui.login.LoginViewModel
import com.jafar.submissionintermediateawal.ui.register.RegisterViewModel
import com.jafar.submissionintermediateawal.utils.preferences.UserPreferences
import com.jafar.submissionintermediateawal.utils.preferences.dataStore

class ViewModelFactory private constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val storyRepository: StoryRepository,
    private val preferences: UserPreferences
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(authenticationRepository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authenticationRepository) as T
        } else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(storyRepository, preferences, authenticationRepository) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(storyRepository) as T
        } else if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown view model class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(
                    Injection.provideAuthRepository(context),
                    Injection.provideStoryRepository(context),
                    UserPreferences.getInstance(context.dataStore)
                ).also {
                    INSTANCE = it
                }
            }
        }
    }
}