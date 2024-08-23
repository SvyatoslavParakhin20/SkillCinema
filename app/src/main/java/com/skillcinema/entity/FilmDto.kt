package com.skillcinema.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FilmsDto(
    @Json(name = "items") override var items: List<FilmDto>?,
    @Json(name = "total") override val total: Int?,
    @Json(name = "category") override var category: String?,
    @Json(name = "filterCategory") override var filterCategory: Int?,
    @Json(name = "films")override var films: List<FilmDto>?
): Films
@JsonClass(generateAdapter = true)
data class FilmDto (
    @Json (name = "kinopoiskId") override var kinopoiskId : Int?,
    @Json (name = "posterUrlPreview") override var posterUrlPreview: String?,
    @Json (name = "countries")override val countries: List<CountryDto>?,
    @Json (name = "duration")override val duration: Int?,
    @Json (name = "genres")override val genres: List<GenreDto>,
    @Json (name = "nameEn")override val nameEn: String?,
    @Json (name = "nameRu")override val nameRu: String?,
    @Json (name = "posterUrl")override val posterUrl: String?,
    @Json (name = "premiereRu")override val premiereRu: String?,
    @Json (name = "year")override val year: Int?,
    @Json(name = "ratingKinopoisk") override val ratingKinopoisk: Double?,
    @Json(name = "filmId") override val filmId: Int?,
    @Json(name = "isWatched") override var isWatched: Boolean = false
):Film
@JsonClass(generateAdapter = true)
data class CountryDto (
    @Json(name = "country") override val country: String
): Country
@JsonClass(generateAdapter = true)
data class GenreDto (
    @Json(name = "genre") override val genre: String
):Genre

