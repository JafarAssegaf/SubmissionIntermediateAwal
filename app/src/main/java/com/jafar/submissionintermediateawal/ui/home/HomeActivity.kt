package com.jafar.submissionintermediateawal.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jafar.submissionintermediateawal.R
import com.jafar.submissionintermediateawal.data.remote.response.ListStoryItem
import com.jafar.submissionintermediateawal.databinding.ActivityMainBinding
import com.jafar.submissionintermediateawal.ui.StoryAdapter
import com.jafar.submissionintermediateawal.ui.ViewModelFactory
import com.jafar.submissionintermediateawal.ui.add_story.AddStoryActivity
import com.jafar.submissionintermediateawal.ui.login.LoginActivity
import com.jafar.submissionintermediateawal.utils.result.Result

class HomeActivity : AppCompatActivity() {

    private lateinit var homeBinding: ActivityMainBinding
    private lateinit var adapter: StoryAdapter

    private var isLogin = true

    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        Log.d("Home", "Yeay masuk")

        homeViewModel.getTokenUser().observe(this) { token ->
            isLogin = token != ""
        }

        if (!isLogin) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            Log.d("Home", "Yaah kok balik lagi")
        }

        adapter = StoryAdapter()

        setUpRecyclerView()
        setUpIntentToAddStory()

        homeViewModel.finalGetAllStory()
        homeViewModel.storyDataState.observe(this) { result ->
            if (result != null) {
                when(result) {
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

    private fun setUpRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        homeBinding.rvListStory.layoutManager = linearLayoutManager
    }

    private fun setUpData(story: List<ListStoryItem?>?) {
        if (story?.isEmpty() == true) {
            homeBinding.tvDataNull.visibility = View.VISIBLE
        } else {
            homeBinding.rvListStory.adapter = adapter
            adapter.submitList(story)
            homeBinding.tvDataNull.visibility = View.GONE
        }
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
                homeViewModel.clearToken()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
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