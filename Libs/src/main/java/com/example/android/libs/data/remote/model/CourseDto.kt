package com.example.android.libs.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.example.android.libs.domain.model.Course

@Serializable
data class CourseDto(
    @SerialName("id") val id: Int,
    @SerialName("downloadid") val downloadId: Int,
    @SerialName("cd_downloads") val cdDownloads: Int,
    @SerialName("title") val title: String,
    @SerialName("status") val status: Int,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("authorid") val authorId: Int,
    @SerialName("video_count") val videoCount: Int,
    @SerialName("educator") val educator: String,
    @SerialName("owned") val owned: Int, // 1 or 0
    @SerialName("sale") val sale: Int,   // 1 or 0
    @SerialName("watched") val watched: Int,
    @SerialName("progress_tracking") val progressTracking: Double,
    @SerialName("style_tags") val styleTags: List<String> = emptyList(),
    @SerialName("skill_tags") val skillTags: List<String> = emptyList(),
    @SerialName("series_tags") val seriesTags: List<String> = emptyList(),
    @SerialName("curriculum_tags") val curriculumTags: List<String> = emptyList()
) {
    fun toDomain(): Course {
        return Course(
            id = id,
            downloadId = downloadId,
            cdDownloads = cdDownloads,
            title = title,
            status = status,
            releaseDate = releaseDate,
            authorId = authorId,
            videoCount = videoCount,
            educator = educator,
            owned = owned == 1,
            sale = sale == 1,
            watched = watched,
            progressTracking = progressTracking,
            styleTags = styleTags.filter { it.isNotBlank() },
            skillTags = skillTags.filter { it.isNotBlank() },
            seriesTags = seriesTags.filter { it.isNotBlank() },
            curriculumTags = curriculumTags.filter { it.isNotBlank() }
        )
    }
}
