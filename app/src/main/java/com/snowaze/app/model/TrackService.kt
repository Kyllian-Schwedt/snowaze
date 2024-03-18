package com.snowaze.app.model

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.firebase.Firebase
import java.util.UUID

interface TrackService {
    val skiLifts: SnapshotStateList<SkiLift>
    val tracks: SnapshotStateList<Track>

    /**
     * Update the status of a track
     * @param id The id of the track
     * @param status The new status of the track (OPENED, CLOSED)
     */
    fun updateTrackStatus(id : UUID, status: Status)

    /**
     * Update the status of a ski lift
     * @param id The id of the ski lift
     * @param status The new status of the ski lift (OPENED, CLOSED)
     */
    fun updateSkiLiftStatus(id : UUID, status: Status)

    /**
     * Add a comment to a track
     * @param id The id of the track
     * @param comment The comment to add
     */
    fun addCommentToTrack(id : UUID, comment: Comment)

    /**
     * Add a comment to a ski lift
     * @param id The id of the ski lift
     * @param comment The comment to add
     */
    fun addCommentToSkiLift(id : UUID, comment: Comment)

    /**
     * Get all the possible path from a point to another
     * @param from The id of the starting point (bottom of the track or top of the ski lift)
     * @param to The id of the ending point (top of the track or bottom of the ski lift)
     * @param maxDifficulty The maximum difficulty of track the person can take
     * @return A list of path
     */
    fun getPath(from: UUID, to: UUID, maxDifficulty: Difficulty = Difficulty.BLACK): List<List<IPath>>
}