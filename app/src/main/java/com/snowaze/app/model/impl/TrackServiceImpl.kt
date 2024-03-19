package com.snowaze.app.model.impl

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.database
import com.snowaze.app.model.Comment
import com.snowaze.app.model.Difficulty
import com.snowaze.app.model.IPath
import com.snowaze.app.model.JsonModel
import com.snowaze.app.model.SkiLift
import com.snowaze.app.model.SkiLiftJSON
import com.snowaze.app.model.SkiLiftType
import com.snowaze.app.model.Status
import com.snowaze.app.model.Track
import com.snowaze.app.model.TrackJSON
import com.snowaze.app.model.TrackService
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackServiceImpl @Inject constructor(): TrackService {
    private var init = false
    private val database = Firebase.database("https://snowaze-default-rtdb.europe-west1.firebasedatabase.app/")

    override val skiLifts: SnapshotStateList<SkiLift> = SnapshotStateList()
    override val tracks: SnapshotStateList<Track> = SnapshotStateList()

    init {
        val ref = this.database.reference
        try {
            ref.get().addOnSuccessListener { it ->
                val value = it.getValue(JsonModel::class.java)
                if (value != null) {
                    // Bind hops
                    val paths = this.tracks + this.skiLifts
                    for (track in this.tracks) {
                        track.hop = paths.filter { it.id.toString() in value.tracks[track.id.toString()]!!.hop }
                    }
                    for (skiLift in this.skiLifts) {
                        skiLift.hop = paths.filter { it.id.toString() in value.skiLifts[skiLift.id.toString()]!!.hop }
                    }
                    ref.child("tracks").addChildEventListener(TrackListener(this.tracks))

                    ref.child("skiLifts").addChildEventListener(SkiLiftListener(this.skiLifts))
                }
            }
        } catch (e: Exception) {
            Log.e("FirebaseService", "Error getting data", e)
        }
    }

    override fun updateTrackStatus(id: UUID, status: Status) {
        this.database.getReference("tracks").child(id.toString()).child("status").setValue(status.toString())
    }

    override fun updateSkiLiftStatus(id: UUID, status: Status) {
        this.database.getReference("skiLifts").child(id.toString()).child("status").setValue(status.toString())
    }

    override fun addCommentToTrack(id: UUID, comment: Comment) {
        this.database.getReference("tracks").child(id.toString()).child("comments").push().setValue(comment)
    }

    override fun addCommentToSkiLift(id: UUID, comment: Comment) {
        this.database.getReference("skiLifts").child(id.toString()).child("comments").push().setValue(comment)
    }

    override fun getPath(from: UUID, to: UUID, maxDifficulty: Difficulty): List<List<IPath>> {
        // A IPath contain a list of neighbour a person can go to
        // This method should return ALL the possible path from the from to the to
        val IPaths = this.tracks + this.skiLifts;
        var paths = mutableListOf<MutableList<IPath>>()
        for (IPath in IPaths) {
            if (IPath.id == from) {
                paths.add(mutableListOf(IPath))
            }
        }
        var i = 0
        while (i < paths.size) {
            val path = paths[i]
            val last = path.last()
            if (last.id == to) {
                i++
                continue
            }
            for (neighbour in last.hop) {
                if (path.contains(neighbour)) {
                    continue
                }
                val newPath = path.toMutableList()
                newPath.add(neighbour)
                paths.add(newPath)
            }
            i++
        }
        paths = paths.filter { it.last().id == to }.map { it.toMutableList() }.toMutableList()
        // Remove the path that go several time to the same place
        for (i in 0 until paths.size) {
            val path = paths[i]
            val set = mutableSetOf<UUID>()
            var valid = true
            for (IPath in path) {
                if (set.contains(IPath.id)) {
                    valid = false
                    break
                }
                set.add(IPath.id)
            }
            if (!valid) {
                paths.removeAt(i)
            }
        }
        // Remove the path that are too difficult
        paths = paths.filter { it.all { it is Track && it.difficulty <= maxDifficulty || it is SkiLift } }.toMutableList()
        // Remove the path that are closed
        paths = paths.filter { it.all { it is Track && it.status == Status.OPEN || it is SkiLift && it.status == Status.OPEN } }.toMutableList()
        // Take the 5 shortest path
        paths.sortBy { it.size }
        if (paths.size > 5) {
            paths = paths.subList(0, 5)
        }

        return paths
    }

    class SkiLiftListener(private val skiLifts: SnapshotStateList<SkiLift>) : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val skiLiftJSON = snapshot.getValue(SkiLiftJSON::class.java)
            if (skiLiftJSON != null) {
                skiLifts.add(
                    SkiLift(
                        id = UUID.fromString(skiLiftJSON.id),
                        name = skiLiftJSON.name,
                        comments = skiLiftJSON.comments,
                        type = SkiLiftType.valueOf(skiLiftJSON.type),
                        status = Status.valueOf(skiLiftJSON.status),
                        hop = mutableListOf()
                    )
                )
            }
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val skiLiftJSON = snapshot.getValue(SkiLiftJSON::class.java)
            if (skiLiftJSON != null) {
                val skiLift = skiLifts.find { it.id == UUID.fromString(skiLiftJSON.id) }
                if (skiLift != null) {
                    skiLift.status = Status.valueOf(skiLiftJSON.status)
                    skiLift.comments = skiLiftJSON.comments
                }
            }
            Log.d("FirebaseService", "SkiLift ${snapshot.key} changed")
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            val skiLiftJSON = snapshot.getValue(SkiLiftJSON::class.java)
            if (skiLiftJSON != null) {
                skiLifts.removeIf { it.id == UUID.fromString(skiLiftJSON.id) }
            }
        }

        override fun onChildMoved(
            snapshot: DataSnapshot,
            previousChildName: String?
        ) {
            TODO("Not yet implemented")
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("FirebaseService", "Error onChildAdded", error.toException())
        }
    }

    class TrackListener(private val tracks: SnapshotStateList<Track>) : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val trackJSON = snapshot.getValue(TrackJSON::class.java)
            if (trackJSON != null) {
                tracks.add(
                    Track(
                        id = UUID.fromString(trackJSON.id),
                        name = trackJSON.name,
                        comments = trackJSON.comments,
                        difficulty = Difficulty.valueOf(trackJSON.difficulty),
                        status = Status.valueOf(trackJSON.status),
                        hop = mutableListOf()
                    )
                )
            }
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val trackJSON = snapshot.getValue(TrackJSON::class.java)
            if (trackJSON != null) {
                val track = tracks.find { it.id == UUID.fromString(trackJSON.id) }
                if (track != null) {
                    track.status = Status.valueOf(trackJSON.status)
                    track.comments = trackJSON.comments
                }
            }
            Log.d("FirebaseService", "Track ${snapshot.key} changed")
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            val trackJSON = snapshot.getValue(TrackJSON::class.java)
            if (trackJSON != null) {
                tracks.removeIf { it.id == UUID.fromString(trackJSON.id) }
            }
        }

        override fun onChildMoved(
            snapshot: DataSnapshot,
            previousChildName: String?
        ) {
            TODO("Not yet implemented")
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("FirebaseService", "Error onChildAdded", error.toException())
        }
    }

}