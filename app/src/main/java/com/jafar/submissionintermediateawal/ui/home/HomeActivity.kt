package com.jafar.submissionintermediateawal.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jafar.submissionintermediateawal.R
import com.jafar.submissionintermediateawal.data.remote.response.ListStoryItem
import com.jafar.submissionintermediateawal.databinding.ActivityMainBinding
import com.jafar.submissionintermediateawal.ui.StoryAdapter
import com.jafar.submissionintermediateawal.ui.ViewModelFactory
import com.jafar.submissionintermediateawal.ui.add_story.AddStoryActivity
import com.jafar.submissionintermediateawal.ui.login.LoginActivity
import com.jafar.submissionintermediateawal.utils.result.Result
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var homeBinding: ActivityMainBinding
    private lateinit var adapter: StoryAdapter

    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        homeViewModel.getTokenUser().observe(this) { token ->
            if (token == null) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        super.onCreate(savedInstanceState)
        homeBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)

        adapter = StoryAdapter()

        setUpRecyclerView()
        setUpIntentToAddStory()

        lifecycleScope.launch {
            homeViewModel.getAllStory().observe(this@HomeActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            val story = result.data
                            setUpData(story)
                        }
                        is Result.Error -> {
                            showLoading(false)
                            showToast(result.error)
                        }
                    }
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        homeBinding.rvListStory.layoutManager = linearLayoutManager
    }

    private fun setUpData(story: List<ListStoryItem?>?) {
        adapter.submitList(story)
        homeBinding.rvListStory.adapter = adapter
    }

    private fun setUpIntentToAddStory() {
        homeBinding.fabAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                // Logika logout
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        homeBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}