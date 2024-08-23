package com.skillcinema.domain

import com.skillcinema.data.FilterGenreDto
import com.skillcinema.entity.ActorDto
import com.skillcinema.entity.ActorGeneralInfoDto
import com.skillcinema.entity.FilmGalleryDto
import com.skillcinema.entity.FilmInfo
import com.skillcinema.entity.FilmSeasonsDto
import com.skillcinema.entity.FilmSimilarsDto
import com.skillcinema.entity.FilmsDto
import com.skillcinema.room.Collection
import com.skillcinema.room.CollectionWIthFilms
import com.skillcinema.room.Film

interface RepositoryInterface
 {
    fun getOnBoardingFlag(): Int
    fun getDataFromSharedPreference(): Int
    fun saveOnboardingFlag():Boolean
    suspend fun getPremieres(page: Int): FilmsDto
    suspend fun getPopular(): FilmsDto
    suspend fun getPopularPaged(page:Int): FilmsDto
    suspend fun getSeries(): FilmsDto
    suspend fun getSeriesPaged(page:Int): FilmsDto
    suspend fun getRandomGenreFilms(genres: FilterGenreDto): FilmsDto
    suspend fun getRandomGenreFilmsPaged(genres: FilterGenreDto, page: Int): FilmsDto
    suspend fun getFilmByKinopoiskId (kinopoiskId: Int): FilmInfo
    suspend fun getTop250(): FilmsDto
    suspend fun getTop250Paged(page: Int): FilmsDto
    suspend fun getActorsByKinopoiskId (kinopoiskId: Int): List<ActorDto>
    suspend fun getImagesByKinopoiskId (kinopoiskId: Int,type:String,page: Int): FilmGalleryDto
    suspend fun getSimilarByKinopoiskId (kinopoiskId: Int): FilmSimilarsDto
    suspend fun getSeasonsByKinopoiskId (kinopoiskId: Int): FilmSeasonsDto
    suspend fun getActorInfoByKinopoiskId (staffId: Int): ActorGeneralInfoDto
    suspend fun getFilmsByFilters (
        countries: List<Int>?,
        genres: List<Int>?,
        order: String,
        type: String,
        ratingFrom: Int,
        ratingTo: Int,
        yearFrom: Int,
        yearTo: Int,
        keyword: String,
        page:Int
    ): FilmsDto
    fun getAllCollections():List<Collection>
    fun getFullCollections(): List<CollectionWIthFilms>
    suspend fun insertCollection(collection: Collection)
    suspend fun  deleteCollection (collection: String)
    suspend fun deleteAllWatched()
    fun getFilmIdsFromCollection(collection: String):List<Int>
    suspend fun insertFilmToDb(film: Film, collection: String)
    suspend fun deleteFilmFromCollection (filmId: Int,collection: String)
    suspend fun cleanCollection(collectionName: String)
    fun getOneFullCollection(collectionName: String): List<CollectionWIthFilms>
}