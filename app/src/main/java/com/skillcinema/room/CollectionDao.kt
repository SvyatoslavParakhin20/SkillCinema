package com.skillcinema.room

import androidx.room.*

@Dao
interface CollectionDao {
    @Transaction
    @Query("SELECT * FROM collection")
    fun getAllCollections (): List<Collection>
    @Transaction
    @Query("SELECT * FROM collection")
    fun getFullCollection (): List<CollectionWIthFilms>
    @Transaction
    @Query("SELECT * FROM collection WHERE name =:collectionName")
    fun getOneFullCollection (collectionName: String): List<CollectionWIthFilms>
    @Insert (entity = Collection::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCollection (collection: Collection)
    @Query ("DELETE FROM collection")
    suspend fun deleteAll ()
    @Query ("DELETE FROM collection WHERE name =:collection")
    suspend fun deleteCollection(collection: String)
    @Query ("DELETE FROM filmCollection WHERE film_id = :filmId AND collection_name =:collectionName")
    suspend fun deleteFilmFromCollection(filmId:Int,collectionName: String)
    @Query("DELETE FROM filmCollection WHERE collection_name =:collectionName")
    suspend fun cleanCollection (collectionName: String)
    @Query ("DELETE FROM film WHERE kinopoisk_id = :kinopoiskId")
    suspend fun deleteFilmFromFilm (kinopoiskId: Int)
    @Insert (entity = Film::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilm (film: Film)
    @Insert (entity = FilmCollection::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFilmToCollection (filmCollection: FilmCollection)
    @Transaction
    @Query("SELECT film_id FROM filmCollection WHERE collection_name = :collectionName")
    fun getCollectionFilmIds (collectionName:String): List<Int>
}