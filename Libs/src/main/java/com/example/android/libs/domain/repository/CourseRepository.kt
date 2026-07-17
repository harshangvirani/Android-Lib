package com.example.android.libs.domain.repository

import com.example.android.libs.domain.model.Course
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    fun getCoursesFlow(): Flow<List<Course>>
    /**
     * Recursive suspending function to fetch courses from the API page-by-page and save to local cache database.
     * Returns a Result containing the accumulated list of all fetched courses.
     */
    suspend fun fetchAndSaveCoursesRecursive(page: Int = 1): Result<List<Course>>
}
