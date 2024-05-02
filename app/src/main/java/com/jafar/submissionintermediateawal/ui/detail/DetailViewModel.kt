package com.jafar.submissionintermediateawal.ui.detail

import androidx.lifecycle.ViewModel
import com.jafar.submissionintermediateawal.data.repository.StoryRepository

class DetailViewModel(
    private val storyRepository: StoryRepository
) : ViewModel() {

    suspend fun getDetailStory(idStory: String) =
        storyRepository.getDetailStory(idStory)

}