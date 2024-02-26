package com.snowaze.app.model

import java.util.UUID

enum class Difficulty {
    GREEN,
    BLUE,
    RED,
    BLACK
}

interface IPath {
    var id: UUID;
    var name: String;
    var hop: List<IPath>;
    var comments: List<Comment>;
}

class Track(
    override var id: UUID,
    override var name: String,
    override var hop: List<IPath>,
    override var comments: List<Comment>,
    val difficulty: Difficulty,
    val status: Status,
) : IPath {
    public fun toJSON(): TrackJSON {
        return TrackJSON(
            id = this.id,
            name = this.name,
            comments = this.comments,
            difficulty = this.difficulty.toString(),
            status = this.status.toString()
        )
    }
}

class SkiLift(
    override var id: UUID,
    override var name: String,
    override var hop: List<IPath>,
    override var comments: List<Comment>,
    val type: SkiLiftType,
    val status: Status,
) : IPath {
    public fun toJSON(): SkiLiftJSON {
        return SkiLiftJSON(
            id = this.id,
            name = this.name,
            comments = this.comments,
            type = this.type.toString(),
            status = this.status.toString()
        )
    }
}

data class Comment(
    val id: String,
    val text: String,
    val author: String,
    val date: String
)