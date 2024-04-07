package com.snowaze.app.model

import java.util.UUID

data class TrackJSON(
    val id: String,
    val name: String,
    val section : Int,
    var comments: HashMap<String, CommentJSON>,
    val difficulty: String,
    val status: String,
    val hop: List<String>,
    val x: Int,
    val y: Int
) {
    constructor() : this("", "", 0, hashMapOf(), "", "", listOf(), 0, 0)
}

data class SkiLiftJSON(
    val id: String,
    val name: String,
    val comments: HashMap<String, CommentJSON>,
    val type: String,
    val status: String,
    val hop: List<String>,
    val x: Int,
    val y: Int
) {
    constructor() : this("", "", hashMapOf(), "", "", listOf(), 0, 0)
}

data class CommentJSON(
    val author: String,
    val text: String,
    val date: String
) {
    constructor() : this("", "", "")
}

data class JsonModel(
    val tracks: Map<String, TrackJSON>,
    val skiLifts: Map<String, SkiLiftJSON>,
) {
    constructor() : this(mapOf(), mapOf())
}

data class ChatMessageJSON(
    val authorId: String,
    val text: String,
    val date: String,
) {
    constructor() : this("", "", "")
}