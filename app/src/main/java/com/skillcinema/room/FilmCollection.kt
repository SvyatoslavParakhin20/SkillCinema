package com.skillcinema.room

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "filmCollection",
    primaryKeys = ["collection_name", "film_id"]
)
data class FilmCollection(
    @ColumnInfo(name = "collection_name")
    val collectionName: String,
    @ColumnInfo(name = "film_id")
    val filmId: Int
)