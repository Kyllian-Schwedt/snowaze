package com.snowaze.app.model

import java.util.UUID

data class TrackJSON(
    val id: String,
    val name: String,
    val section : Int,
    val comments: List<Comment>,
    val difficulty: String,
    val status: String,
    val hop: List<String>
) {
    constructor() : this("", "", 0, listOf(), "", "", listOf())
}

data class SkiLiftJSON(
    val id: String,
    val name: String,
    val comments: List<Comment>,
    val type: String,
    val status: String,
    val hop: List<String>
) {
    constructor() : this("", "", listOf(), "", "", listOf())
}

data class JsonModel(
    val tracks: Map<String, TrackJSON>,
    val skiLifts: Map<String, SkiLiftJSON>,
) {
    constructor() : this(mapOf(), mapOf())
}