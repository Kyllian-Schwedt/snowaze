package com.snowaze.app.services

import android.util.Log
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
import java.util.UUID

class FirebaseService {
    companion object {
        private var init = false
        private val database = Firebase.database("https://snowaze-default-rtdb.europe-west1.firebasedatabase.app/")
        val skiLifts : MutableList<SkiLift> = mutableListOf()
        val tracks : MutableList<Track> = mutableListOf()

        /**
         * Initialize the FirebaseService and start listening to the database. This method should be called once in the application lifecycle.
         */
        fun init() {
            if (init) return
            init = true
            val ref = database.reference;
            // Get the data once
            try {
                ref.get().addOnSuccessListener {
                    val value = it.getValue(JsonModel::class.java)
                    if (value != null) {
                        // Get the tracks
                        for (track in value.tracks) {
                            val trackJSON = track.value
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
                        // Get the skiLifts
                        for (skiLift in value.skiLifts) {
                            val skiLiftJSON = skiLift.value
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
                        // Bind hops
                        val paths = tracks + skiLifts
                        for (track in tracks) {
                            track.hop = paths.filter { it.id.toString() in value.tracks[track.id.toString()]!!.hop }
                        }
                        for (skiLift in skiLifts) {
                            skiLift.hop = paths.filter { it.id.toString() in value.skiLifts[skiLift.id.toString()]!!.hop }
                        }
                        // Log
                        Log.d("FirebaseService", "${tracks.size} tracks and ${skiLifts.size} skiLifts loaded")
                        Log.d("FirebaseService", "Tracks Hops 1: ${tracks[0].hop.size}")
                        Log.d("FirebaseService", "SkiLifts Hops 1: ${skiLifts[0].hop.size}")
                        // Add listener on child for tracks
                        val trackEventListener = object : ChildEventListener {
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
                        ref.child("tracks").addChildEventListener(trackEventListener)
                        // Add listener on child for skiLifts
                        val childEventListener = object : ChildEventListener {
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
                        // Add listener on child for skiLifts
                        ref.child("skiLifts").addChildEventListener(childEventListener)
                        val path = getPath(UUID.fromString("87bf4a3c-563d-4796-8733-4ce05998e089"), UUID.fromString("362c471b-e5d2-4896-94b0-eefff3d8e695"), Difficulty.GREEN)
                        for (p in path) {
                            Log.d("FirebaseService", "Path: ${p.map { it.name }}")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("FirebaseService", "Error getting data", e)
            }
        }

        /**
         * Update the status of a track
         * @param id The id of the track
         * @param status The new status of the track (OPENED, CLOSED)
         */
        fun updateTrackStatus(id : UUID, status: Status) {
            database.getReference("tracks").child(id.toString()).child("status").setValue(status.toString())
        }

        /**
         * Update the status of a ski lift
         * @param id The id of the ski lift
         * @param status The new status of the ski lift (OPENED, CLOSED)
         */
        fun updateSkiLiftStatus(id : UUID, status: Status) {
            database.getReference("skiLifts").child(id.toString()).child("status").setValue(status.toString())
        }

        /**
         * Add a comment to a track
         * @param id The id of the track
         * @param comment The comment to add
         */
        fun addCommentToTrack(id : UUID, comment: Comment) {
            database.getReference("tracks").child(id.toString()).child("comments").push().setValue(comment)
        }

        /**
         * Add a comment to a ski lift
         * @param id The id of the ski lift
         * @param comment The comment to add
         */
        fun addCommentToSkiLift(id : UUID, comment: Comment) {
            database.getReference("skiLifts").child(id.toString()).child("comments").push().setValue(comment)
        }

        /**
         * Get all the possible path from a point to another
         * @param from The id of the starting point (bottom of the track or top of the ski lift)
         * @param to The id of the ending point (top of the track or bottom of the ski lift)
         * @param maxDifficulty The maximum difficulty of track the person can take
         * @return A list of path
         */
        fun getPath(from: UUID, to: UUID, maxDifficulty: Difficulty = Difficulty.BLACK): List<List<IPath>> {
            // A IPath contain a list of neighbour a person can go to
            // This method should return ALL the possible path from the from to the to
            val IPaths = tracks + skiLifts;
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
            // Take the 5 shortest path
            paths.sortBy { it.size }
            if (paths.size > 5) {
                paths = paths.subList(0, 5)
            }

            return paths
        }
    }
}
