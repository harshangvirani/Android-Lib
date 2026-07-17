package com.example.android.libs.di

import com.example.android.libs.data.repository.CourseRepositoryImpl
import com.example.android.libs.domain.repository.CourseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCourseRepository(
        repositoryImpl: CourseRepositoryImpl
    ): CourseRepository
}
