package com.snowaze.app.model

import java.util.UUID

data class TrackJSON(
    val id: String,
    val name: String,
    val section : Int,
    var comments: HashMap<String, CommentJSON>,
    val difficulty: String,
    val status: String,
    val hop: List<String>
) {
    constructor() : this("", "", 0, hashMapOf(), "", "", listOf())
}

data class SkiLiftJSON(
    val id: String,
    val name: String,
    val comments: HashMap<String, CommentJSON>,
    val type: String,
    val status: String,
    val hop: List<String>
) {
    constructor() : this("", "", hashMapOf(), "", "", listOf())
}

data class CommentJSON(
    val id: String,
    val author: String,
    val text: String,
    val date: String
) {
    constructor() : this("","", "", "")
}

data class JsonModel(
    val tracks: Map<String, TrackJSON>,
    val skiLifts: Map<String, SkiLiftJSON>,
) {
    constructor() : this(mapOf(), mapOf())
}