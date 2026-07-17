package com.example.android.lib

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.lib.databinding.ItemCourseBinding
import com.example.android.libs.domain.model.Course
import coil.load

class CourseAdapter(private val onItemClick: (Course) -> Unit) : ListAdapter<Course, CourseAdapter.CourseViewHolder>(DiffCallback) {

    private val gradients = listOf(
        R.drawable.gradient_blue,
        R.drawable.gradient_purple,
        R.drawable.gradient_orange,
        R.drawable.gradient_green,
        R.drawable.gradient_red
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemCourseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CourseViewHolder(
        private val binding: ItemCourseBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(course: Course) {
            binding.courseTitle.text = course.title
            binding.courseEducator.text = course.educator

            // Show tilted "Owned" banner if course is owned
            binding.ownedBadge.visibility = if (course.owned) View.VISIBLE else View.GONE

            // Style the card background with a unique premium gradient based on course ID
            val backgroundRes = gradients[Math.abs(course.id) % gradients.size]
            binding.cardBackground.setBackgroundResource(backgroundRes)

            // Extract the first word or numbers from the title to display in center of the card
            val abbreviation = extractAbbreviation(course.title)
            binding.courseAbbreviation.text = abbreviation

            // Load dynamically using Coil with caching enabled by default
            val imageUrl = "https://picsum.photos/seed/${course.title.hashCode()}/300/300"
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

            binding.root.setOnClickListener {
                onItemClick(course)
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

    companion object DiffCallback : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem == newItem
        }
    }
}
