package com.skillcinema.data

import android.content.Context
import android.content.SharedPreferences
import com.skillcinema.domain.RepositoryInterface
import com.skillcinema.entity.ActorDto
import com.skillcinema.entity.ActorGeneralInfoDto
import com.skillcinema.entity.FilmGalleryDto
import com.skillcinema.entity.FilmInfo
import com.skillcinema.entity.FilmSeasonsDto
import com.skillcinema.entity.FilmSimilarsDto
import com.skillcinema.entity.FilmsDto
import com.skillcinema.room.Collection
import com.skillcinema.room.CollectionDao
import com.skillcinema.room.Film
import com.skillcinema.room.FilmCollection
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class FakeRepositoryImpl @Inject constructor(
    private val api: Api,
    @ApplicationContext
    val context: Context,
    private val collectionDao: CollectionDao
    ): RepositoryInterface {
        private lateinit var prefs: SharedPreferences
        private var onboardingShown = 0
        override fun getOnBoardingFlag(): Int {

            return when {
                onboardingShown == 1 -> 1

                getDataFromSharedPreference() == 1 -> {
                    onboardingShown = 1
                    1
                }
                else -> {
                    saveOnboardingFlag()
                    0
                }
            }
        }

        override fun getDataFromSharedPreference(): Int {
            prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            return prefs.getInt(KEY_INT_NAME, 0)

        }
        override fun saveOnboardingFlag():Boolean {
            onboardingShown = 1
            prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = prefs.edit()
            editor.putInt(KEY_INT_NAME,1)
            editor.apply()
            return true
        }

        override suspend fun getPremieres(page: Int): FilmsDto {
            return api.getPremieres(page)
        }
        override suspend fun getPopular(): FilmsDto {
            return api.getPopular(1)
        }
        override suspend fun getPopularPaged(page:Int): FilmsDto {
            return api.getPopular(page)
        }
        override suspend fun getSeries(): FilmsDto {
            return api.getSeries(1)
        }
        override suspend fun getSeriesPaged(page:Int): FilmsDto {
            return api.getSeries(page)
        }
        override suspend fun getRandomGenreFilms(genres:FilterGenreDto): FilmsDto {
            return api.getRandomGenreFilms(genres,page=1)
        }
        override suspend fun getRandomGenreFilmsPaged(genres:FilterGenreDto, page: Int): FilmsDto {
            return api.getRandomGenreFilms(genres,page)
        }
        override suspend fun getFilmByKinopoiskId (kinopoiskId: Int): FilmInfo {
            return api.getFilmByKinopoiskId(kinopoiskId)
        }
        override suspend fun getTop250(): FilmsDto {
            return api.getTop250Films(1)
        }
        override suspend fun getTop250Paged(page: Int): FilmsDto {
            return api.getTop250Films(page)
        }
        override suspend fun getActorsByKinopoiskId (kinopoiskId: Int): List<ActorDto> {
            return api.getActorsByKinopoiskId(kinopoiskId)
        }
        override suspend fun getImagesByKinopoiskId (kinopoiskId: Int, type:String, page: Int): FilmGalleryDto {
            return api.getImagesByKinopoiskId(kinopoiskId,type,page)
        }
        override suspend fun getSimilarByKinopoiskId (kinopoiskId: Int): FilmSimilarsDto {
            return api.getSimilarByKinopoiskId(kinopoiskId)
        }
        override suspend fun getSeasonsByKinopoiskId (kinopoiskId: Int): FilmSeasonsDto {
            return api.getSeasonsByKinopoiskId(kinopoiskId)
        }
        override suspend fun getActorInfoByKinopoiskId (staffId: Int): ActorGeneralInfoDto {
            return api.getActorInfoByKinopoiskId(staffId)
        }
        override suspend fun getFilmsByFilters (
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
        ): FilmsDto {
            return api.getFilmsbyFilters(countries, genres, order, type, ratingFrom, ratingTo, yearFrom, yearTo, keyword, page)
        }

        override fun getAllCollections() = collectionDao.getAllCollections()
        override fun getFullCollections() = collectionDao.getFullCollection()
        override suspend fun insertCollection(collection: Collection) = collectionDao.insertCollection (collection = collection)
        override suspend fun  deleteCollection (collection: String){
            collectionDao.cleanCollection(collectionName = collection)
            collectionDao.deleteCollection(collection = collection)
        }
        override suspend fun deleteAllWatched() = collectionDao.deleteAll()

        override fun getFilmIdsFromCollection(collection: String) = collectionDao.getCollectionFilmIds(collection)

        override suspend fun insertFilmToDb(film: Film, collection: String) {
            collectionDao.insertFilm(film)
            collectionDao.insertFilmToCollection(
                filmCollection =  FilmCollection(
                    collectionName = collection,
                    filmId = film.kinopoiskId
                )
            )
        }
        override suspend fun deleteFilmFromCollection (filmId: Int, collection: String) {
            collectionDao.deleteFilmFromCollection(filmId = filmId, collectionName = collection)
        }
        override suspend fun cleanCollection(collectionName: String){
            collectionDao.cleanCollection(collectionName = collectionName)
        }
        override fun getOneFullCollection(collectionName: String) = collectionDao.getOneFullCollection(collectionName)

        companion object{
            private  const val PREFERENCE_NAME = "prefs_name"
            private const val KEY_INT_NAME = "KEY_STRING"
        }
    }