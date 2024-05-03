package com.jafar.submissionintermediateawal.data.repository

import com.jafar.submissionintermediateawal.data.remote.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val apiService: ApiService
){
    suspend fun finalGetAllStory() =
        apiService.getAllUser()

    suspend fun finalGetDetailStory(idStory: String) =
        apiService.getDetailStory(idStory)

    suspend fun finalPostStory(description: RequestBody, image: MultipartBody.Part) =
        apiService.postStory(description, image)


    companion object {
        @Volatile
        private var INSTANCE: StoryRepository? = null

        fun getInstance(apiService: ApiService): StoryRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: StoryRepository(apiService).also {
                    INSTANCE = it
                }
            }
        }
    }

}