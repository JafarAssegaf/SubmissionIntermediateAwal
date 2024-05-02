package com.jafar.submissionintermediateawal.ui

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jafar.submissionintermediateawal.data.remote.response.ListStoryItem
import com.jafar.submissionintermediateawal.databinding.ItemStoryBinding
import com.jafar.submissionintermediateawal.ui.detail.DetailActivity
import com.jafar.submissionintermediateawal.utils.helper.StoryDiffCallback

class StoryAdapter : ListAdapter<ListStoryItem, StoryAdapter.ViewHolder>(StoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            with(binding) {
                tvItemTitle.text = story.name
                tvItemDescription.text = story.description
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .centerCrop()
                    .into(ivItemThumbnail)
                itemView.setOnClickListener {
                    val intentToDetail = Intent(itemView.context, DetailActivity::class.java)
                    intentToDetail.putExtra(DetailActivity.EXTRA_ID_STORY, story.id)

                    val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(ivItemThumbnail, "story_image"),
                        Pair(tvItemTitle, "story_name"),
                        Pair(tvItemDescription, "story_description")
                    )
                    itemView.context.startActivity(intentToDetail, optionsCompat.toBundle())
                }
            }
        }
    }
}