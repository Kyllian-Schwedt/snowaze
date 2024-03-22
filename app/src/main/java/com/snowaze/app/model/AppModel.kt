package com.snowaze.app.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import java.util.UUID
import java.util.Date

interface IPath {
    var id: UUID;
    var name: String;
    var hop: List<IPath>;
    var comments: HashMap<UUID, Comment>;
}

class Track (
    override var id: UUID,
    override var name: String,
    override var hop: List<IPath>,
    override var comments: HashMap<UUID, Comment>,
    var section: Int,
    val difficulty: Difficulty,
    var status: MutableState<Status>
) : IPath {
    public fun toJSON(): TrackJSON {
        val hop : List<String> = if (this.hop.isEmpty()) {
            listOf()
        } else {
            this.hop.map { it.id.toString() }
        }
        return TrackJSON(
            id = this.id.toString(),
            name = this.name,
            section = this.section,
            comments = this.comments.map { it.key.toString() to it.value.toJson() }.toMap() as HashMap<String, CommentJSON>,
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
    override var comments: HashMap<UUID, Comment>,
    var type: SkiLiftType,
    var status: Status,
) : IPath {
    public fun toJSON(): SkiLiftJSON {
        val hop : List<String> = if (this.hop.isEmpty()) {
            listOf()
        } else {
            this.hop.map { it.id.toString() }
        }
        return SkiLiftJSON(
            id = this.id.toString(),
            name = this.name,
            comments = this.comments.map { it.key.toString() to it.value.toJson() }.toMap() as HashMap<String, CommentJSON>,
            type = this.type.toString(),
            status = this.status.toString(),
            hop = hop
        )
    }
}

class Comment(
    var id: UUID,
    var text: String,
    var authorId: String,
    var date: Date
) {
    constructor() : this(UUID.randomUUID(), "", "", Date())
    constructor(text: String, authorId: String) : this(UUID.randomUUID(), text, authorId, Date())
    public fun toJson(): CommentJSON {
        return CommentJSON(
            id = this.id.toString(),
            text = this.text,
            author = this.authorId,
            date = this.date.toString()
        )
    }

    constructor(json: CommentJSON) : this(UUID.fromString(json.id), json.text, json.author, Date(json.date))
}