package com.skillcinema.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FilmSimilarsDto(
    @Json val items: List<FilmSimilarsItemDto>?,
    @Json val total: Int?
)
@JsonClass(generateAdapter = true)
data class FilmSimilarsItemDto(
    @Json val filmId: Int?,
    @Json val nameEn: String?,
    @Json val nameOriginal: String?,
    @Json val nameRu: String?,
    @Json val posterUrl: String?,
    @Json val posterUrlPreview: String?,
    @Json val relationType: String?,
    @Json var isWatched: Boolean = false
)