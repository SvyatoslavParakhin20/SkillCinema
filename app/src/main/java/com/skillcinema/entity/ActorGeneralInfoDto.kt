package com.skillcinema.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ActorGeneralInfoDto(
    @Json val age: Int?,
    @Json val birthday: String?,
    @Json val birthplace: String?,
    @Json val death: String?,
    @Json val deathplace: String?,
    @Json val facts: List<Any>?,
    @Json val films: List<ActorFilmDto>?,
    @Json val growth: Int?,
    @Json val hasAwards: Int?,
    @Json val nameEn: String?,
    @Json val nameRu: String?,
    @Json val personId: Int?,
    @Json val posterUrl: String?,
    @Json val profession: String?,
    @Json val sex: String?,
    @Json val spouses: List<SpouseDto>?,
    @Json val webUrl: String?
)
@JsonClass(generateAdapter = true)
data class ActorFilmDto(
    @Json val description: String?,
    @Json val filmId: Int?,
    @Json val general: Boolean?,
    @Json val nameEn: String?,
    @Json val nameRu: String?,
    @Json val professionKey: String?,
    @Json val rating: String?
)
@JsonClass(generateAdapter = true)
data class SpouseDto(
    @Json val children: Int?,
    @Json val divorced: Boolean?,
    @Json val divorcedReason: String?,
    @Json val name: String?,
    @Json val personId: Int?,
    @Json val relation: String?,
    @Json val sex: String?,
    @Json val webUrl: String?
)