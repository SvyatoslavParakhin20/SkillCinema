package com.skillcinema.room

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class CollectionWIthFilms(
    @Embedded
    val collection: @RawValue Collection,
    @Relation(
        parentColumn = "name",
        entityColumn = "kinopoisk_id",
        associateBy = Junction(
            FilmCollection::class,
            parentColumn = "collection_name",
            entityColumn = "film_id"
        )
    )
    val films:@RawValue List<Film>,
):Parcelable
