package com.snowaze.app.model

enum class Difficulty {

    GREEN,
    BLUE,
    RED,
    BLACK;

    val value: String
        get() = when (this) {
            GREEN -> "Green"
            BLUE -> "Blue"
            RED -> "Red"
            BLACK -> "Black"
        }
}