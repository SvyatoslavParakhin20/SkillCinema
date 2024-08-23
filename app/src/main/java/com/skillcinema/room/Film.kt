package com.skillcinema.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "film")
data class Film(
    @PrimaryKey
    @ColumnInfo(name = "kinopoisk_id") val kinopoiskId:Int,
    val duration: Int?,
    val nameEn: String?,
    val nameRu: String?,
    val posterUrl: String?,
    val posterUrlPreview: String?,
    val premiereRu: String?,
    val year: Int?,
    val ratingKinopoisk: Double?,
    val filmId: Int?,
    var isWatched: Boolean?,
    val countries: String?,
    val genres: String?,
    val isLiked: Boolean?,
    val toWatch: Boolean?
)
