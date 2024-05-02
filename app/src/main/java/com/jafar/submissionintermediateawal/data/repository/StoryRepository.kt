package com.jafar.submissionintermediateawal.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.jafar.submissionintermediateawal.data.remote.response.ErrorResponse
import com.jafar.submissionintermediateawal.data.remote.response.ListStoryItem
import com.jafar.submissionintermediateawal.data.remote.response.Story
import com.jafar.submissionintermediateawal.data.remote.retrofit.ApiService
import com.jafar.submissionintermediateawal.utils.result.Result
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class StoryRepository private constructor(
    private val apiService: ApiService
){

    private val resultHome = MutableLiveData<Result<List<ListStoryItem?>?>>()
    private val resultDetail = MutableLiveData<Result<Story?>>()
    private val resultPost = MutableLiveData<Result<String?>>()

    suspend fun getAllStory(): LiveData<Result<List<ListStoryItem?>?>> {
        resultHome.value = Result.Loading
        try {
            val response = apiService.getAllUser()
            val listStory = response.listStory
            resultHome.value = Result.Success(listStory)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            resultHome.value = Result.Error(errorMessage.toString())
        }
        return resultHome
    }

    suspend fun getDetailStory(idStory: String): LiveData<Result<Story?>> {
        resultDetail.value = Result.Loading
        try {
            val response = apiService.getDetailStory(idStory)
            val data = response.story
            resultDetail.value = Result.Success(data)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            resultDetail.value = Result.Error(errorMessage.toString())
        }
        return resultDetail
    }

    suspend fun postStory(description: RequestBody, image: MultipartBody.Part): LiveData<Result<String?>> {
        resultPost.value = Result.Loading
        try {
            val response = apiService.postStory(description, image)
            val data = response.message
            resultPost.value = Result.Success(data)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            resultPost.value = Result.Error(errorMessage.toString())
        }
        return resultPost
    }

    companion object {
        @Volatile
        private var INSTANCE: StoryRepository? = null

        fun getInstance(apiService: ApiService): StoryRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: StoryRepository(apiService)
            }
        }
    }

}