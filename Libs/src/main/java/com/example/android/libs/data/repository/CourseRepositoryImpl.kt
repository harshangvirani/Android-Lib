package com.example.android.libs.data.repository

import com.example.android.libs.data.local.dao.CourseDao
import com.example.android.libs.data.local.entity.CourseEntity
import com.example.android.libs.data.remote.CourseApiService
import com.example.android.libs.domain.model.Course
import com.example.android.libs.domain.repository.CourseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CourseRepositoryImpl @Inject constructor(
    private val apiService: CourseApiService,
    private val courseDao: CourseDao
) : CourseRepository {

    override fun getCoursesFlow(): Flow<List<Course>> {
        return courseDao.getCourses().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    /**
     * This is a recursive suspending function.
     * 
     * - Suspending: It pauses execution without blocking the host thread during remote I/O (using Kotlin coroutines).
     * - Recursive: It resolves paginated network data by calling itself recursively with the next page index (page + 1)
     *   until it reaches the base case (no more results returned or page exceeds MAX_PAGES).
     */
    override suspend fun fetchAndSaveCoursesRecursive(page: Int): Result<List<Course>> {
        return runCatching {
            // Fetch courses for the current page from remote client
            val response = apiService.getCourses(page)
            val courseDtos = response.result.index

            // Check base case: if list is empty or we hit our simulation limit, we stop
            if (courseDtos.isNotEmpty() && page <= MAX_PAGES) {
                // Map DTOs to Room Entities
                val domainCourses = courseDtos.map { it.toDomain() }
                val entities = domainCourses.map { CourseEntity.fromDomain(it) }
                // Save to Room DB
                courseDao.insertCourses(entities)

                // Recursive call for the next page and merge the results
                val nextPageList = fetchAndSaveCoursesRecursive(page + 1).getOrThrow()
                domainCourses + nextPageList
            } else {
                emptyList()
            }
        }
    }

    companion object {
        private const val MAX_PAGES = 3 // Simulates pagination limit on static mock URL
    }
}
