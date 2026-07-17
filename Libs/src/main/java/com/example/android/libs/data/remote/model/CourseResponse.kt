package com.example.android.libs.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseResponse(
    @SerialName("result") val result: ResultDto
)

@Serializable
data class ResultDto(
    @SerialName("index") val index: List<CourseDto>
)
