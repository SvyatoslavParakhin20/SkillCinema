package com.skillcinema.ui.search

object SearchSettings {
    var countries: List<Int>? = null
    var genres: List<Int>? = null
    var order: String = "RATING"
    var type: String = "ALL"
    var ratingFrom: Int = 1
    var ratingTo: Int = 10
    var yearFrom: Int = 1000
    var yearTo: Int = 3000
    var keyword: String = ""
    var notWatchedOnly = false
}