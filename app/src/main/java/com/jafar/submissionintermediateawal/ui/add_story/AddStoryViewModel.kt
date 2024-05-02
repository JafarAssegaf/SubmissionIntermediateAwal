package com.jafar.submissionintermediateawal.ui.add_story

import androidx.lifecycle.ViewModel
import com.jafar.submissionintermediateawal.data.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(
    private val storyRepository: StoryRepository
) : ViewModel() {

    suspend fun postStory(description: RequestBody, image: MultipartBody.Part) =
        storyRepository.postStory(description, image)
}