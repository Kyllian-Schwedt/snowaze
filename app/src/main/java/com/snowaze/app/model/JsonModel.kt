package com.snowaze.app.model

import java.util.UUID

data class TrackJSON(
    val id: UUID,
    val name: String,
    val comments: List<Comment>,
    val difficulty: String,
    val status: String
)

data class SkiLiftJSON(
    val id: UUID,
    val name: String,
    val comments: List<Comment>,
    val type: String,
    val status: String
)

data class HopJSON(
    val id: UUID,
    val hop : List<UUID>
)

data class JsonModel(
    val tracks: List<TrackJSON>,
    val skiLifts: List<SkiLiftJSON>,
    val hops: List<HopJSON>
)