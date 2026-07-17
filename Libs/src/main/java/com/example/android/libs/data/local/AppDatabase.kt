package com.example.android.libs.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android.libs.data.local.dao.CourseDao
import com.example.android.libs.data.local.entity.CourseEntity

@Database(entities = [CourseEntity::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
}
