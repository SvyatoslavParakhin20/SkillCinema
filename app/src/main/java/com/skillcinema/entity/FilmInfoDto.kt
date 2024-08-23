package com.skillcinema.entity

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
@JsonClass(generateAdapter = true)
data class FilmInfo(
    @Json (name = "completed") val completed: Boolean?,
    @Json (name = "countries") val countries: List<CountryInfo>,
    @Json (name = "coverUrl") val coverUrl: @RawValue Any?,
    @Json (name = "description") val description: String?,
    @Json (name = "editorAnnotation") val editorAnnotation: @RawValue Any?,
    @Json (name = "endYear") val endYear: @RawValue Any?,
    @Json (name = "filmLength") val filmLength: Int?,
    @Json (name = "genres") val genres: List<GenreInfo>,
    @Json (name = "has3D") val has3D: Boolean?,
    @Json (name = "hasImax") val hasImax: Boolean?,
    @Json (name = "imdbId") val imdbId: @RawValue Any?,
    @Json (name = "isTicketsAvailable") val isTicketsAvailable: Boolean?,
    @Json (name = "kinopoiskId") val kinopoiskId: Int?,
    @Json (name = "lastSync") val lastSync: String?,
    @Json (name = "logoUrl") val logoUrl: @RawValue Any?,
    @Json (name = "nameEn") val nameEn: @RawValue Any?,
    @Json (name = "nameOriginal") val nameOriginal: @RawValue Any?,
    @Json (name = "nameRu") val nameRu: String?,
    @Json (name = "posterUrl") val posterUrl: String?,
    @Json (name = "posterUrlPreview") val posterUrlPreview: String?,
    @Json (name = "productionStatus") val productionStatus: @RawValue Any?,
    @Json (name = "ratingAgeLimits") val ratingAgeLimits: @RawValue Any?,
    @Json (name = "ratingAwait") val ratingAwait: @RawValue Any?,
    @Json (name = "ratingAwaitCount") val ratingAwaitCount: Int?,
    @Json (name = "ratingFilmCritics") val ratingFilmCritics: @RawValue Any?,
    @Json (name = "ratingFilmCriticsVoteCount") val ratingFilmCriticsVoteCount: Int?,
    @Json (name = "ratingGoodReview") val ratingGoodReview: Double?,
    @Json (name = "ratingGoodReviewVoteCount") val ratingGoodReviewVoteCount: Int?,
    @Json (name = "ratingImdb") val ratingImdb: @RawValue Any?,
    @Json (name = "ratingImdbVoteCount") val ratingImdbVoteCount: Int?,
    @Json (name = "ratingKinopoisk") val ratingKinopoisk: Double?,
    @Json (name = "ratingKinopoiskVoteCount") val ratingKinopoiskVoteCount: Int?,
    @Json (name = "ratingMpaa") val ratingMpaa: @RawValue Any?,
    @Json (name = "ratingRfCritics") val ratingRfCritics: @RawValue Any?,
    @Json (name = "ratingRfCriticsVoteCount") val ratingRfCriticsVoteCount: Int?,
    @Json (name = "reviewsCount") val reviewsCount: Int?,
    @Json (name = "serial") val serial: Boolean?,
    @Json (name = "shortDescription") val shortDescription: @RawValue Any?,
    @Json (name = "shortFilm") val shortFilm: Boolean?,
    @Json (name = "slogan") val slogan: @RawValue Any?,
    @Json (name = "startYear") val startYear: @RawValue Any?,
    @Json (name = "type") val type: String?,
    @Json (name = "webUrl") val webUrl: String?,
    @Json (name = "year") val year: Int?,
    @Json (name = "isWatched") var isWatched: Boolean = false,
    @Json (name = "isLiked") var isLiked: Boolean = false,
    @Json (name = "toWatch") var toWatch:Boolean = false
):Parcelable
@Parcelize
@JsonClass(generateAdapter = true)
data class CountryInfo(
    @Json (name = "country") val country: String?
):Parcelable
@Parcelize
@JsonClass(generateAdapter = true)
data class GenreInfo(
    @Json (name = "genre") val genre: String?
):Parcelable