package com.skillcinema.entity


interface Films {
    val items: List<Film>?
    val total: Int?
    val category: String?
    val filterCategory: Int?
    val films:List<Film>?
}
interface Film {
    val countries: List<Country>?
    val duration: Int?
    val genres: List<Genre>
    val kinopoiskId: Int?
    val nameEn: String?
    val nameRu: String?
    val posterUrl: String?
    val posterUrlPreview: String?
    val premiereRu: String?
    val year: Int?
    val ratingKinopoisk: Double?
    val filmId: Int?
    var isWatched: Boolean
}
interface Country {
    val country: String?
}
interface Genre {
    val genre: String?
}