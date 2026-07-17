package com.example.android.libs.domain.model

data class Course(
    val id: Int,
    val downloadId: Int,
    val cdDownloads: Int,
    val title: String,
    val status: Int,
    val releaseDate: String,
    val authorId: Int,
    val videoCount: Int,
    val educator: String,
    val owned: Boolean,
    val sale: Boolean,
    val watched: Int,
    val progressTracking: Double,
    val styleTags: List<String>,
    val skillTags: List<String>,
    val seriesTags: List<String>,
    val curriculumTags: List<String>
)
