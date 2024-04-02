package com.snowaze.app.model

data class User(
    open val id: String = "",
    val isAnonymous: Boolean = true
)

class UserDetail (
    val firstName: String = "",
    val lastName: String = "",
    val pseudo: String = "",
    val maxTrackDifficulty: Difficulty = Difficulty.BLACK,
    val minTrackDifficulty: Difficulty = Difficulty.GREEN,
    val favoriteTracks: List<String> = emptyList(),
    val photoUrl: String = ""
)
