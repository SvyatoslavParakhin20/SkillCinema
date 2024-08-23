package com.skillcinema.data

import android.content.Context
import com.skillcinema.R
import com.skillcinema.entity.ActorDto
import com.skillcinema.entity.ActorGeneralInfoDto
import com.skillcinema.entity.FilmGalleryDto
import com.skillcinema.entity.FilmInfo
import com.skillcinema.entity.FilmSeasonsDto
import com.skillcinema.entity.FilmSimilarsDto
import com.skillcinema.entity.FilmsDto
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

private const val BASE_URL = "https://kinopoiskapiunofficial.tech/"
private const val API_KEY = "dd4acd93-2462-4a8d-a990-abc7223761c5"
class Api @Inject constructor(
    @ApplicationContext
    val context: Context
) {
    object RetrofitServices {
        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val searchPremiereApi: SearchPremiereApi = retrofit.create(SearchPremiereApi::class.java)
        val searchPopularApi: SearchPopularApi = retrofit.create(SearchPopularApi::class.java)
        val searchSeriesApi: SearchSeriesApi = retrofit.create(SearchSeriesApi::class.java)
        val searchRandomGenreApi: SearchRandomGenreApi = retrofit.create(SearchRandomGenreApi::class.java)
        val searchTop250Api: SearchTop250Api = retrofit.create(SearchTop250Api::class.java)
        val searchFilmInfoByKinopoiskIdApi: SearchFilmByKinopoiskIdApi = retrofit.create(SearchFilmByKinopoiskIdApi::class.java)
        val searchActorsApi: SearchActorsByKinopoiskIdApi = retrofit.create(SearchActorsByKinopoiskIdApi::class.java)
        val searchImagesByKinopoiskIdApi: SearchImagesByKinopoiskIdApi = retrofit.create(SearchImagesByKinopoiskIdApi::class.java)
        val searchSimilarByKinopoiskIdApi: SearchSimilarByKinopoiskIdApi = retrofit.create(SearchSimilarByKinopoiskIdApi::class.java)
        val searchSeasonsByKinopoiskIdApi: SearchSeasonsByKinopoiskIdApi = retrofit.create(SearchSeasonsByKinopoiskIdApi::class.java)
        val searchActorInfoByKinopoiskStaffIdApi: SearchActorInfoByKinopoiskStaffIdApi = retrofit.create(SearchActorInfoByKinopoiskStaffIdApi::class.java)
        val searchFilmsByFiltersApi: SearchFilmsByFiltersApi = retrofit.create(SearchFilmsByFiltersApi::class.java)
    }
    interface SearchPremiereApi {
        @Headers(
            "X-API-KEY:$API_KEY",
            "Content-Type: application/json"
        )
        @GET("api/v2.2/films/premieres")
        suspend fun getPremieresList(
            @Query(value = "year") year: Int,
            @Query(value = "month") month: String,
            @Query(value = "page") page: Int
        ): FilmsDto
    }
    interface SearchPopularApi {
        @Headers(
            "X-API-KEY:$API_KEY",
            "Content-Type: application/json"
        )
        @GET("api/v2.2/films")
        suspend fun getPopularList(
            @Query(value = "ratingFrom") ratingFrom: Int,
            @Query(value = "page") page: Int
        ): FilmsDto
    }
    interface SearchSeriesApi {
        @Headers(
            "X-API-KEY:$API_KEY",
            "Content-Type: application/json"
        )
        @GET("api/v2.2/films")
        suspend fun getSeriesList(
            @Query(value = "type") type: String,
            @Query(value = "page") page: Int
        ): FilmsDto
    }
    interface SearchRandomGenreApi {
        @Headers(
            "X-API-KEY:$API_KEY",
            "Content-Type: application/json"
        )
        @GET("api/v2.2/films")
        suspend fun getRandomGenreList(
            @Query(value = "genres") genres: Int,
            @Query(value = "page") page: Int
        ): FilmsDto
    }
    interface SearchTop250Api {
        @Headers(
            "X-API-KEY:$API_KEY",
            "Content-Type: application/json"
        )
        @GET("api/v2.2/films/top")
        suspend fun getTop250(
            @Query(value = "type") type: String,
            @Query(value = "page") page: Int
        ): FilmsDto
    }
    interface SearchFilmByKinopoiskIdApi {
        @Headers(
            "X-API-KEY:$API_KEY",
            "Content-Type: application/json"
        )
        @GET("api/v2.2/films/{id}")
        suspend fun getFilmByKinopoiskId(
            @Path(value = "id") id: Int
        ): FilmInfo
    }
    interface SearchActorsByKinopoiskIdApi {
        @Headers(
            "X-API-KEY:$API_KEY",
            "Content-Type: application/json"
        )
        @GET("api/v1/staff")
        suspend fun getFilmByKinopoiskId(
            @Query(value = "filmId") filmId: Int
        ): List<ActorDto>
    }
    interface SearchImagesByKinopoiskIdApi {
        @Headers(
            "X-API-KEY:$API_KEY",
            "Content-Type: application/json"
        )
        @GET("api/v2.2/films/{id}/images")
        suspend fun getImagesByKinopoiskId(
            @Path(value = "id") id: Int,
            @Query (value = "type") type: String,
            @Query(value = "page") page: Int
        ): FilmGalleryDto
    }
    interface SearchSimilarByKinopoiskIdApi {
        @Headers(
            "X-API-KEY:$API_KEY",
            "Content-Type: application/json"
        )
        @GET("api/v2.2/films/{id}/similars")
        suspend fun getSimilarByKinopoiskId(
            @Path(value = "id") id: Int
        ): FilmSimilarsDto
    }
    interface SearchSeasonsByKinopoiskIdApi {
        @Headers(
            "X-API-KEY:$API_KEY",
            "Content-Type: application/json"
        )
        @GET("api/v2.2/films/{id}/seasons")
        suspend fun getSeasonsByKinopoiskId(
            @Path(value = "id") id: Int
        ): FilmSeasonsDto
    }
    interface SearchActorInfoByKinopoiskStaffIdApi {
        @Headers(
            "X-API-KEY:$API_KEY",
            "Content-Type: application/json"
        )
        @GET("api/v1/staff/{id}")
        suspend fun getActorInfoByKinopoiskId(
            @Path(value = "id") id: Int
        ): ActorGeneralInfoDto
    }

    interface SearchFilmsByFiltersApi {
        @Headers(
            "X-API-KEY:$API_KEY",
            "Content-Type: application/json"
        )
        @GET("api/v2.2/films")
        suspend fun getFilmsByFilters(
            @Query(value = "countries") countries: List<Int>?,  // JUST 1 country is supported
            @Query(value = "genres") genres: List<Int>?, // JUST 1 genre is supported
            @Query(value = "order") order: String,
            @Query(value = "type") type: String,
            @Query(value = "ratingFrom") ratingFrom: Int,
            @Query(value = "ratingTo") ratingTo: Int,
            @Query(value = "yearFrom") yearFrom: Int,
            @Query(value = "yearTo") yearTo: Int,
            @Query(value = "keyword") keyword: String,
            @Query(value = "page") page:Int
        ): FilmsDto
    }
    suspend fun getPremieres(page: Int): FilmsDto {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance()
            .getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)
        val premieres = RetrofitServices.searchPremiereApi.getPremieresList(year, month!!,page)
        premieres.category =  context.getString(R.string.Premieres)
        premieres.filterCategory = 1111
        return premieres
    }
    suspend fun getPopular(page: Int): FilmsDto {
        val popular = RetrofitServices.searchPopularApi.getPopularList(6,page)
        popular.category = context.getString(R.string.Popular)
        popular.filterCategory = 2222
        return popular
    }
    suspend fun getSeries(page: Int): FilmsDto {
        val series = RetrofitServices.searchSeriesApi.getSeriesList("TV_SERIES",page)
        series.category = context.getString(R.string.Series)
        series.filterCategory = 3333
        return series
    }
    suspend fun getTop250Films (page: Int): FilmsDto {
        val top250 = RetrofitServices.searchTop250Api.getTop250(type = "TOP_250_BEST_FILMS",page)
        top250.category = context.getString(R.string.Top250)
        top250.filterCategory = 4444
        top250.items= top250.films
        top250.items?.forEach { it.kinopoiskId = it.filmId }
        return top250
    }
    suspend fun getRandomGenreFilms(genres: FilterGenreDto,page:Int): FilmsDto {
        val genre = RetrofitServices.searchRandomGenreApi.getRandomGenreList(genres.id!!,page=page)
        genre.category = genres.genre.toString().replaceFirstChar { it.uppercase() }
        genre.filterCategory = genres.id
        return genre
    }
    suspend fun getFilmByKinopoiskId(kinopoiskId: Int): FilmInfo {
        return RetrofitServices.searchFilmInfoByKinopoiskIdApi.getFilmByKinopoiskId(kinopoiskId)
    }
    suspend fun getActorsByKinopoiskId(kinopoiskId: Int): List<ActorDto> {
        return RetrofitServices.searchActorsApi.getFilmByKinopoiskId(kinopoiskId)
    }
    suspend fun getImagesByKinopoiskId(kinopoiskId: Int,type: String,page: Int): FilmGalleryDto {
        return RetrofitServices.searchImagesByKinopoiskIdApi.getImagesByKinopoiskId(kinopoiskId,type,page)
    }
    suspend fun getSimilarByKinopoiskId(kinopoiskId: Int): FilmSimilarsDto {
        return RetrofitServices.searchSimilarByKinopoiskIdApi.getSimilarByKinopoiskId(kinopoiskId)
    }
    suspend fun getSeasonsByKinopoiskId(kinopoiskId: Int): FilmSeasonsDto {
        return RetrofitServices.searchSeasonsByKinopoiskIdApi.getSeasonsByKinopoiskId(kinopoiskId)
    }
    suspend fun getActorInfoByKinopoiskId(staffId: Int): ActorGeneralInfoDto {
        return RetrofitServices.searchActorInfoByKinopoiskStaffIdApi.getActorInfoByKinopoiskId(staffId)
    }
    suspend fun getFilmsbyFilters(
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
        return RetrofitServices.searchFilmsByFiltersApi.getFilmsByFilters(countries, genres, order, type, ratingFrom, ratingTo, yearFrom, yearTo, keyword, page)
    }
}