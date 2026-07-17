package com.example.android.lib

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.android.lib.databinding.ActivityMainBinding
import com.example.android.libs.domain.model.Course
import com.example.android.libs.presentation.CourseUiState
import com.example.android.libs.presentation.CourseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: CourseViewModel by viewModels()

    private val ownedAdapter by lazy { CourseAdapter { showCourseDetails(it) } }
    private val recentlyWatchedAdapter by lazy { CourseAdapter { showCourseDetails(it) } }
    private val favoritesAdapter by lazy { CourseAdapter { showCourseDetails(it) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerViews()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerViews() {
        binding.rvOwned.adapter = ownedAdapter
        binding.rvRecentlyWatched.adapter = recentlyWatchedAdapter
        binding.rvFavorites.adapter = favoritesAdapter
    }

    private fun setupListeners() {
        binding.btnRetry.setOnClickListener {
            viewModel.refreshCourses()
        }

        binding.tvViewAllOwned.setOnClickListener {
            navigateToAllCourses("Owned")
        }

        binding.tvViewAllRecentlyWatched.setOnClickListener {
            navigateToAllCourses("Recently Watched")
        }

        binding.tvViewAllFavorites.setOnClickListener {
            navigateToAllCourses("Favorites")
        }
    }

    private fun navigateToAllCourses(category: String) {
        val intent = Intent(this, AllCoursesActivity::class.java).apply {
            putExtra("category", category)
        }
        startActivity(intent)
    }

    private fun showCourseDetails(course: Course) {
        val intent = Intent(this, CourseDetailsActivity::class.java).apply {
            putExtra("course_json", Json.encodeToString(course))
        }
        startActivity(intent)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is CourseUiState.Loading -> {
                            binding.loadingIndicator.visibility = View.VISIBLE
                            binding.errorView.visibility = View.GONE
                            binding.contentView.visibility = View.GONE
                        }
                        is CourseUiState.Success -> {
                            binding.loadingIndicator.visibility = View.GONE
                            binding.errorView.visibility = View.GONE
                            binding.contentView.visibility = View.VISIBLE

                            // Filter and display owned courses
                            val ownedCourses = state.courses.filter { it.owned }
                            ownedAdapter.submitList(ownedCourses)
                            binding.ownedHeader.visibility = if (ownedCourses.isNotEmpty()) View.VISIBLE else View.GONE
                            binding.rvOwned.visibility = if (ownedCourses.isNotEmpty()) View.VISIBLE else View.GONE

                            // Filter and display recently watched courses
                            val recentlyWatched = state.courses.filter { it.watched > 0 }
                            recentlyWatchedAdapter.submitList(recentlyWatched)
                            binding.recentlyWatchedHeader.visibility = if (recentlyWatched.isNotEmpty()) View.VISIBLE else View.GONE
                            binding.rvRecentlyWatched.visibility = if (recentlyWatched.isNotEmpty()) View.VISIBLE else View.GONE

                            // Filter and display favorite courses
                            val favorites = state.courses.filter { it.isFavorite }
                            favoritesAdapter.submitList(favorites)
                            binding.favoritesHeader.visibility = if (favorites.isNotEmpty()) View.VISIBLE else View.GONE
                            binding.rvFavorites.visibility = if (favorites.isNotEmpty()) View.VISIBLE else View.GONE
                        }
                        is CourseUiState.Error -> {
                            binding.loadingIndicator.visibility = View.GONE
                            binding.errorView.visibility = View.VISIBLE
                            binding.contentView.visibility = View.GONE
                            binding.errorText.text = state.message
                        }
                    }
                }
            }
        }
    }
}