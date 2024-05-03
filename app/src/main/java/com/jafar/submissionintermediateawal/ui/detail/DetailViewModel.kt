package com.jafar.submissionintermediateawal.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.jafar.submissionintermediateawal.data.remote.response.ErrorResponse
import com.jafar.submissionintermediateawal.data.remote.response.Story
import com.jafar.submissionintermediateawal.data.repository.StoryRepository
import kotlinx.coroutines.launch
import com.jafar.submissionintermediateawal.utils.result.Result
import retrofit2.HttpException

class DetailViewModel(
    private val storyRepository: StoryRepository
) : ViewModel() {

    private val _detailStoryState = MutableLiveData<Result<Story?>>()
    val detailStoryState: LiveData<Result<Story?>>
        get() = _detailStoryState

    fun finalGetDetailStory(idStory: String) = viewModelScope.launch {
        _detailStoryState.value = Result.Loading
        try {
            val detailResponse = storyRepository.finalGetDetailStory(idStory)
            val dataDetailStory = detailResponse.story
            _detailStoryState.value = Result.Success(dataDetailStory)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message.toString()
            _detailStoryState.value = Result.Error(errorMessage)
        }
    }
}