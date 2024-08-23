package com.skillcinema.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collection")
data class Collection(
    @PrimaryKey
    @ColumnInfo(name = "name")
    val name: String
)
