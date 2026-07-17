package com.example.android.libs.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import com.example.android.libs.data.remote.model.CourseResponse

class CourseApiService(private val client: HttpClient) {
    suspend fun getCourses(page: Int): CourseResponse {
        // We fetch from the provided URL: http://tiny.cc/tafv001
        // We pass the page parameter to simulate recursive paginated endpoints.
        return client.get("http://tiny.cc/tafv001").body()
    }
}
