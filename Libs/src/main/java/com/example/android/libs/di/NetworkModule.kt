package com.example.android.libs.di

import com.example.android.libs.data.remote.CourseApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.http.ContentType
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
            install(ContentNegotiation) {
                val jsonConfig = Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                    prettyPrint = true
                }
                json(jsonConfig)
                json(jsonConfig, contentType = ContentType.Text.Plain)
            }
        }
    }

    @Provides
    @Singleton
    fun provideCourseApiService(client: HttpClient): CourseApiService {
        return CourseApiService(client)
    }
}
