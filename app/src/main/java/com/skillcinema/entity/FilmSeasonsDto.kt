package com.skillcinema.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FilmSeasonsDto(
    @Json val items: List<Item>,
    @Json val total: Int?
)
@JsonClass(generateAdapter = true)
data class Item(
    @Json val episodes: List<Episode>?,
    @Json val number: Int?
)
@JsonClass(generateAdapter = true)
data class Episode(
    @Json val episodeNumber: Int?,
    @Json val nameEn: String?,
    @Json val nameRu: String?,
    @Json val releaseDate: String?,
    @Json val seasonNumber: Int?,
    @Json val synopsis: String?
)