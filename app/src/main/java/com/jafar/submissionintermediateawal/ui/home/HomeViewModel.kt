package com.jafar.submissionintermediateawal.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.jafar.submissionintermediateawal.data.remote.response.ErrorResponse
import com.jafar.submissionintermediateawal.data.remote.response.ListStoryItem
import com.jafar.submissionintermediateawal.data.repository.AuthenticationRepository
import com.jafar.submissionintermediateawal.data.repository.StoryRepository
import com.jafar.submissionintermediateawal.utils.preferences.UserPreferences
import kotlinx.coroutines.launch
import com.jafar.submissionintermediateawal.utils.result.Result
import retrofit2.HttpException

class HomeViewModel(
    private val storyRepository: StoryRepository,
    private val preferences: UserPreferences,
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private val _storyDataState = MutableLiveData<Result<List<ListStoryItem>>>()
    val storyDataState: LiveData<Result<List<ListStoryItem>>>
        get() = _storyDataState

    fun finalGetAllStory() = viewModelScope.launch {
        _storyDataState.value = Result.Loading

        try {
            val storyResponse = storyRepository.finalGetAllStory()
            val listStory = storyResponse.listStory
            _storyDataState.value = Result.Success(listStory)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message.toString()
            _storyDataState.value = Result.Error(errorMessage)
        }
    }

    fun getTokenUser(): LiveData<String> {
        return authenticationRepository.getUserToken().asLiveData()
    }

    fun clearToken() = viewModelScope.launch {
        authenticationRepository.clearToken()
    }
}
