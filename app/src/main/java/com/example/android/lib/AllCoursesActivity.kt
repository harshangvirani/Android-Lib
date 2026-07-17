package com.example.android.lib

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.android.lib.databinding.ActivityAllCoursesBinding
import com.example.android.libs.domain.model.Course
import com.example.android.libs.presentation.CourseUiState
import com.example.android.libs.presentation.CourseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class AllCoursesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllCoursesBinding
    private val viewModel: CourseViewModel by viewModels()
    private val courseAdapter by lazy { CourseAdapter { showCourseDetails(it) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllCoursesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val category = intent.getStringExtra("category") ?: "Courses"
        binding.tvTitle.text = category

        setupRecyclerView()
        setupListeners()
        observeViewModel(category)
    }

    private fun setupRecyclerView() {
        binding.rvCoursesGrid.adapter = courseAdapter
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun showCourseDetails(course: Course) {
        val intent = Intent(this, CourseDetailsActivity::class.java).apply {
            putExtra("course_json", Json.encodeToString(course))
        }
        startActivity(intent)
    }

    private fun observeViewModel(category: String) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is CourseUiState.Loading -> {
                            binding.loadingIndicator.visibility = View.VISIBLE
                            binding.rvCoursesGrid.visibility = View.GONE
                        }
                        is CourseUiState.Success -> {
                            binding.loadingIndicator.visibility = View.GONE
                            binding.rvCoursesGrid.visibility = View.VISIBLE

                            // Filter data depending on chosen category
                            val filteredCourses = when (category) {
                                "Owned" -> state.courses.filter { it.owned }
                                "Recently Watched" -> state.courses.filter { it.watched > 0 }
                                "Favorites" -> state.courses.filter { it.isFavorite }
                                else -> state.courses
                            }
                            courseAdapter.submitList(filteredCourses)
                        }
                        is CourseUiState.Error -> {
                            binding.loadingIndicator.visibility = View.GONE
                            binding.rvCoursesGrid.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }
}
