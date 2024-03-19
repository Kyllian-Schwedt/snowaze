package com.snowaze.app.model

import java.util.UUID

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
    var section: Int,
    val difficulty: Difficulty,
    var status: Status,
) : IPath {
    public fun toJSON(): TrackJSON {
        val hop : List<String>;
        if (this.hop.isEmpty()) {
            hop = listOf()
        } else {
            hop = this.hop.map { it.id.toString() }
        }
        return TrackJSON(
            id = this.id.toString(),
            name = this.name,
            section = this.section,
            comments = this.comments,
            difficulty = this.difficulty.toString(),
            status = this.status.toString(),
            hop = hop
        )
    }
}

class SkiLift(
    override var id: UUID,
    override var name: String,
    override var hop: List<IPath>,
    override var comments: List<Comment>,
    var type: SkiLiftType,
    var status: Status,
) : IPath {
    public fun toJSON(): SkiLiftJSON {
        val hop : List<String>;
        if (this.hop.isEmpty()) {
            hop = listOf()
        } else {
            hop = this.hop.map { it.id.toString() }
        }
        return SkiLiftJSON(
            id = this.id.toString(),
            name = this.name,
            comments = this.comments,
            type = this.type.toString(),
            status = this.status.toString(),
            hop = hop
        )
    }
}

data class Comment(
    val id: String,
    val text: String,
    val author: String,
    val date: String
)