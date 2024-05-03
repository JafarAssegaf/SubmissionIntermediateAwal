package com.jafar.submissionintermediateawal.ui.add_story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.jafar.submissionintermediateawal.data.remote.response.ErrorResponse
import com.jafar.submissionintermediateawal.utils.result.Result
import com.jafar.submissionintermediateawal.data.repository.StoryRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class AddStoryViewModel(
    private val storyRepository: StoryRepository
) : ViewModel() {

    private val _postStoryState = MutableLiveData<Result<String?>>()
    val postStoryState: LiveData<Result<String?>>
        get() = _postStoryState

    fun finalPostStory(description: RequestBody, image: MultipartBody.Part) = viewModelScope.launch {
        _postStoryState.value = Result.Loading

        try {
            val postStoryResponse = storyRepository.finalPostStory(description, image)
            val messageSuccess = postStoryResponse.message
            _postStoryState.value = Result.Success(messageSuccess)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message.toString()
            _postStoryState.value = Result.Error(errorMessage)
        }
    }
}