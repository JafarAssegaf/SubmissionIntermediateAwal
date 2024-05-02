package com.jafar.submissionintermediateawal.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.jafar.submissionintermediateawal.data.remote.response.Story
import com.jafar.submissionintermediateawal.databinding.ActivityDetailBinding
import com.jafar.submissionintermediateawal.ui.ViewModelFactory
import com.jafar.submissionintermediateawal.utils.result.Result
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idStory = intent.getStringExtra(EXTRA_ID_STORY)

        if (idStory != null) {
            lifecycleScope.launch {
                detailViewModel.getDetailStory(idStory).observe(this@DetailActivity) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                showLoading(true)
                            }
                            is Result.Success -> {
                                showLoading(false)
                                showDetailData(result.data)
                            }
                            is Result.Error -> {
                                showLoading(false)
                                Log.e(TAG, "Error: ${result.error}")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showDetailData(story: Story?) {
        Glide.with(this)
            .load(story?.photoUrl)
            .centerCrop()
            .into(binding.ivDetailImage)

        binding.tvDetailName.text = story?.name
        binding.tvDetailDescription.text = story?.description
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private var TAG = DetailActivity::class.java.simpleName
        const val EXTRA_ID_STORY = "extra_id_story"
    }
}