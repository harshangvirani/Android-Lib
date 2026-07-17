package com.example.android.lib

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.android.lib.databinding.ActivityCourseDetailsBinding
import com.example.android.libs.domain.model.Course
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class CourseDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCourseDetailsBinding

    private val gradients = listOf(
        R.drawable.gradient_blue,
        R.drawable.gradient_purple,
        R.drawable.gradient_orange,
        R.drawable.gradient_green,
        R.drawable.gradient_red
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val courseJson = intent.getStringExtra("course_json")
        if (courseJson != null) {
            try {
                val course = Json.decodeFromString<Course>(courseJson)
                bindCourseDetails(course)
            } catch (e: Exception) {
                finish()
            }
        } else {
            finish()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun bindCourseDetails(course: Course) {
        binding.tvTitle.text = course.title
        binding.tvEducator.text = course.educator
        binding.tvVideos.text = "${course.videoCount} Videos"
        binding.tvReleaseDate.text = if (course.releaseDate.length >= 10) {
            course.releaseDate.substring(0, 10)
        } else {
            course.releaseDate
        }
        binding.tvProgress.text = "${course.progressTracking}%"

        // Ownership state text
        binding.tvOwnership.text = when {
            course.owned -> {
                binding.tvOwnership.setTextColor(resources.getColor(android.R.color.holo_green_light, theme))
                "Owned"
            }
            course.sale -> {
                binding.tvOwnership.setTextColor(resources.getColor(android.R.color.holo_orange_light, theme))
                "On Sale"
            }
            else -> {
                binding.tvOwnership.setTextColor(resources.getColor(android.R.color.darker_gray, theme))
                "Not Owned"
            }
        }

        // Style & Skill tags display formatting
        val allTags = mutableListOf<String>()
        allTags.addAll(course.styleTags)
        allTags.addAll(course.skillTags)
        allTags.addAll(course.seriesTags)
        allTags.addAll(course.curriculumTags)
        val filteredTags = allTags.filter { it.isNotBlank() }.distinct()

        binding.tvTags.text = if (filteredTags.isNotEmpty()) {
            filteredTags.joinToString("\n") { "• $it" }
        } else {
            "No tags available"
        }

        // Apply fallback gradient
        val backgroundRes = gradients[Math.abs(course.id) % gradients.size]
        binding.cardBackground.setBackgroundResource(backgroundRes)

        // Set course initials abbreviation overlay
        val initials = extractAbbreviation(course.title)
        binding.courseAbbreviation.text = initials

        // Load dynamic Picsum seed cover image with Coil
        val imageUrl = "https://picsum.photos/seed/${course.title.hashCode()}/400/300"
        binding.courseImage.load(imageUrl) {
            crossfade(true)
            listener(
                onStart = {
                    binding.courseAbbreviation.visibility = View.VISIBLE
                },
                onSuccess = { _, _ ->
                    binding.courseAbbreviation.visibility = View.GONE
                },
                onError = { _, _ ->
                    binding.courseAbbreviation.visibility = View.VISIBLE
                }
            )
        }
    }

    private fun extractAbbreviation(title: String): String {
        val words = title.trim().split("\\s+".toRegex())
        if (words.isEmpty()) return ""
        val firstWord = words.first()
        if (firstWord.all { it.isDigit() }) {
            return firstWord
        }
        if (words.size >= 2) {
            return "${words[0].first().uppercase()}${words[1].first().uppercase()}"
        }
        return firstWord.take(3).uppercase()
    }
}
