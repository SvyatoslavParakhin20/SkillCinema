package com.skillcinema.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        Collection::class,
        FilmCollection::class,
        Film::class,
    ],
    version = 1, exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmDao(): CollectionDao
}