package com.snowaze.app.model

import androidx.compose.runtime.snapshots.SnapshotStateList
import java.util.UUID
import javax.inject.Singleton

@Singleton
interface TrackService {
    val skiLifts: SnapshotStateList<SkiLift>
    val tracks: SnapshotStateList<Track>
    val chatMessages: SnapshotStateList<ChatMessage>

    /**
     * Update the status of a track
     * @param id The id of the track
     * @param status The new status of the track (OPENED, CLOSED)
     */
    fun updateTrackStatus(id : UUID, status: Status)

    /**
     * Get a track by its id
     * @param id The id of the track
     */
    fun getTrack(id: UUID): Track?

    /**
     * Get a ski lift by its id
     * @param id The id of the ski lift
     */
    fun getSkiLift(id: UUID): SkiLift?

    /**
     * Update the status of a ski lift
     * @param id The id of the ski lift
     * @param status The new status of the ski lift (OPENED, CLOSED)
     */
    fun updateSkiLiftStatus(id : UUID, status: Status)

    /**
     * Add a comment to a track
     * @param id The id of the track
     * @param text The comment to add
     * @param author The author of the comment
     */
    fun addCommentToTrack(id : UUID, text: String, author: String)

    /**
     * Add a comment to a ski lift
     * @param id The id of the ski lift
     * @param text The comment to add
     * @param author The author of the comment
     */
    fun addCommentToSkiLift(id : UUID, text: String, author: String)

    /**
     * Add a chat message
     * @param text The text of the message
     * @param author The author of the message
     */
    fun addChatMessage(text: String, author: String)

    /**
     * Get all the possible path from a point to another
     * @param from The id of the starting point (bottom of the track or top of the ski lift)
     * @param to The id of the ending point (top of the track or bottom of the ski lift)
     * @param maxDifficulty The maximum difficulty of track the person can take
     * @return A list of path
     */
    fun getPath(from: UUID, to: UUID, maxDifficulty: Difficulty = Difficulty.BLACK): List<List<IPath>>
}