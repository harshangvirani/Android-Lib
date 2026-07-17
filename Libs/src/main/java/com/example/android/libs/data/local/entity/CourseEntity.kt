package com.example.android.libs.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.libs.domain.model.Course

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "download_id") val downloadId: Int,
    @ColumnInfo(name = "cd_downloads") val cdDownloads: Int,
    val title: String,
    val status: Int,
    @ColumnInfo(name = "release_date") val releaseDate: String,
    @ColumnInfo(name = "author_id") val authorId: Int,
    @ColumnInfo(name = "video_count") val videoCount: Int,
    val educator: String,
    val owned: Boolean,
    val sale: Boolean,
    val watched: Int,
    @ColumnInfo(name = "progress_tracking") val progressTracking: Double,
    @ColumnInfo(name = "style_tags") val styleTags: List<String>,
    @ColumnInfo(name = "skill_tags") val skillTags: List<String>,
    @ColumnInfo(name = "series_tags") val seriesTags: List<String>,
    @ColumnInfo(name = "curriculum_tags") val curriculumTags: List<String>,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean
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
            owned = owned,
            sale = sale,
            watched = watched,
            progressTracking = progressTracking,
            styleTags = styleTags,
            skillTags = skillTags,
            seriesTags = seriesTags,
            curriculumTags = curriculumTags,
            isFavorite = isFavorite
        )
    }

    companion object {
        fun fromDomain(course: Course): CourseEntity {
            return CourseEntity(
                id = course.id,
                downloadId = course.downloadId,
                cdDownloads = course.cdDownloads,
                title = course.title,
                status = course.status,
                releaseDate = course.releaseDate,
                authorId = course.authorId,
                videoCount = course.videoCount,
                educator = course.educator,
                owned = course.owned,
                sale = course.sale,
                watched = course.watched,
                progressTracking = course.progressTracking,
                styleTags = course.styleTags,
                skillTags = course.skillTags,
                seriesTags = course.seriesTags,
                curriculumTags = course.curriculumTags,
                isFavorite = course.isFavorite
            )
        }
    }
}
