package com.example.android.libs.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.libs.data.local.entity.CourseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses ORDER BY title ASC")
    fun getCourses(): Flow<List<CourseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourses(courses: List<CourseEntity>)

    @Query("DELETE FROM courses")
    suspend fun clearCourses()

    @Query("SELECT COUNT(*) FROM courses")
    suspend fun getCoursesCount(): Int

    @Query("SELECT * FROM courses ORDER BY title ASC")
    suspend fun getCoursesOnce(): List<CourseEntity>
}
