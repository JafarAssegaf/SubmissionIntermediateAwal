package com.jafar.submissionintermediateawal.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.jafar.submissionintermediateawal.data.repository.StoryRepository
import com.jafar.submissionintermediateawal.utils.preferences.UserPreferences

class HomeViewModel(
    private val storyRepository: StoryRepository,
    private val preferences: UserPreferences
) : ViewModel() {

    suspend fun getAllStory() = storyRepository.getAllStory()

    fun getTokenUser(): LiveData<String> {
        return preferences.getUserToken().asLiveData()
    }

}
