package com.jafar.submissionintermediateawal.utils.helper

import androidx.recyclerview.widget.DiffUtil
import com.jafar.submissionintermediateawal.data.remote.response.ListStoryItem

class StoryDiffCallback : DiffUtil.ItemCallback<ListStoryItem>() {
    override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
        return oldItem == newItem
    }
}