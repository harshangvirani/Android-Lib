package com.example.android.libs

import com.example.android.libs.data.local.Converters
import com.example.android.libs.data.remote.model.CourseDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CourseDataTest {

    @Test
    fun converters_serializesAndDeserializesCorrectly() {
        val converters = Converters()
        val originalList = listOf("Blues", "Rock", "Country")

        // Serialize
        val serializedString = converters.fromStringList(originalList)
        assertEquals("Blues||Rock||Country", serializedString)

        // Deserialize
        val deserializedList = converters.toStringList(serializedString)
        assertEquals(3, deserializedList.size)
        assertEquals("Blues", deserializedList[0])
        assertEquals("Rock", deserializedList[1])
        assertEquals("Country", deserializedList[2])
    }

    @Test
    fun converters_handlesEmptyAndNullLists() {
        val converters = Converters()

        // Empty list serialization
        val emptySerialized = converters.fromStringList(emptyList())
        assertEquals("", emptySerialized)

        // Empty deserialization
        val emptyDeserialized = converters.toStringList("")
        assertTrue(emptyDeserialized.isEmpty())

        // Null deserialization
        val nullDeserialized = converters.toStringList(null)
        assertTrue(nullDeserialized.isEmpty())
    }

    @Test
    fun courseDto_toDomain_mapsCorrectly() {
        // Given a DTO
        val dto = CourseDto(
            id = 48,
            downloadId = 9326,
            cdDownloads = 2,
            title = "50 Blues Rhythms You MUST Know",
            status = 1,
            releaseDate = "2013-01-24 00:00:00",
            authorId = 4200,
            videoCount = 54,
            educator = "Corey Congilio",
            owned = 1,
            sale = 1,
            watched = 43,
            progressTracking = 5.5,
            styleTags = listOf("Blues"),
            skillTags = listOf("Intermediate"),
            seriesTags = listOf("Licks"),
            curriculumTags = listOf("Rhythm")
        )

        // When mapping to domain model
        val domain = dto.toDomain()

        // Then verify mapping values
        assertEquals(48, domain.id)
        assertEquals(9326, domain.downloadId)
        assertEquals("50 Blues Rhythms You MUST Know", domain.title)
        assertTrue(domain.owned)
        assertTrue(domain.sale)
        assertEquals(5.5, domain.progressTracking, 0.001)
        assertEquals("Blues", domain.styleTags.first())
    }
}
