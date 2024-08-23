package com.skillcinema.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FilmGalleryDto(
    @Json val items: List<FilmGalleryItemDto>?,
    @Json val total: Int?,
    @Json val totalPages: Int?
)
@JsonClass(generateAdapter = true)
data class FilmGalleryItemDto(
    @Json val imageUrl: String?,
    @Json val previewUrl: String?
)