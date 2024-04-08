package com.snowaze.app.model.impl

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.database
import com.snowaze.app.model.ChatMessage
import com.snowaze.app.model.ChatMessageJSON
import com.snowaze.app.model.Comment
import com.snowaze.app.model.CommentJSON
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
import com.snowaze.app.screens.map.ImageMarker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackServiceImpl @Inject constructor(): TrackService {
    private var init = false
    private val database = Firebase.database("https://snowaze-default-rtdb.europe-west1.firebasedatabase.app/")

    override val skiLifts: SnapshotStateList<SkiLift> = SnapshotStateList()
    override val tracks: SnapshotStateList<Track> = SnapshotStateList()
    override val chatMessages: SnapshotStateList<ChatMessage> = SnapshotStateList()

    private val _markers = MutableStateFlow<List<ImageMarker>>(emptyList())
    override val markers: Flow<List<ImageMarker>> = _markers.asStateFlow()

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
                    ref.child("tracks").addChildEventListener(TrackListener(this.tracks, this._markers
                    ) { this.fetchAllHops() })

                    ref.child("skiLifts").addChildEventListener(SkiLiftListener(this.skiLifts, this._markers){
                        this.fetchAllHops()
                    })
                    ref.child("chatMessages").addChildEventListener(ChatMessagesListener(this.chatMessages))

                    tracks.forEach { track ->
                        val newMarker = ImageMarker(track.x, track.y, mutableStateOf(track))
                        track.marker = newMarker
                        _markers.value += newMarker
                    }
                    skiLifts.forEach { skiLift ->
                        val newMarker = ImageMarker(skiLift.x, skiLift.y, mutableStateOf(skiLift))
                        skiLift.marker = newMarker
                        _markers.value += newMarker
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("FirebaseService", "Error getting data", e)
        }
    }

    override fun updateTrackStatus(id: UUID, status: Status) {
        this.database.getReference("tracks").child(id.toString()).child("status").setValue(status.toString())
    }

    override fun getTrack(id: UUID): Track? {
        return this.tracks.find { it.id == id }
    }

    override fun getSkiLift(id: UUID): SkiLift? {
        return this.skiLifts.find { it.id == id }
    }

    override fun updateSkiLiftStatus(id: UUID, status: Status) {
        this.database.getReference("skiLifts").child(id.toString()).child("status").setValue(status.toString())
    }

    override fun addCommentToTrack(id: UUID, text:String, author: String) {
        val comment = Comment(text, author)
        this.database.getReference("tracks").child(id.toString()).child("comments").push().setValue(comment.toJson())
    }

    override fun addCommentToSkiLift(id: UUID, text:String, author: String) {
        val comment = Comment(text, author)
        this.database.getReference("skiLifts").child(id.toString()).child("comments").push().setValue(comment.toJson())
    }

    override fun addChatMessage(text: String, author: String) {
        val message = ChatMessage(text, author)
        this.database.getReference("chatMessages").push().setValue(message.toJson())
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
        paths = paths.filter { it.all { it is Track && it.status.value == Status.OPEN || it is SkiLift && it.status.value == Status.OPEN } }.toMutableList()
        // Take the 5 shortest path
        paths.sortBy { it.size }
        if (paths.size > 5) {
            paths = paths.subList(0, 5)
        }

        return paths
    }

    class SkiLiftListener(private val skiLifts: SnapshotStateList<SkiLift>, private val markers: MutableStateFlow<List<ImageMarker>>, private val callback: () -> Unit) : ChildEventListener, CommentsHashMapToList() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d("FirebaseService", "SkiLift ${snapshot.key} added")
            if (snapshot.value == null) {
                return
            }
            try {
                val skiLiftJSON = snapshot.getValue(SkiLiftJSON::class.java)
                if (skiLiftJSON != null) {
                    skiLifts.add(
                        SkiLift(
                            id = UUID.fromString(skiLiftJSON.id),
                            name = skiLiftJSON.name,
                            comments = commentsHashMapToList(skiLiftJSON.comments),
                            type = SkiLiftType.valueOf(skiLiftJSON.type),
                            status = try { mutableStateOf(Status.valueOf(skiLiftJSON.status)) } catch (e: Exception) {
                                Log.e("FirebaseService", "Error parsing Status", e)
                                mutableStateOf(Status.UNKNOWN)
                            },
                            hop = mutableListOf(),
                            x = skiLiftJSON.x,
                            y = skiLiftJSON.y,
                            hopIds = skiLiftJSON.hop.map { UUID.fromString(it) }
                        )
                    )
                    Log.d("FirebaseService", "SkiLift ${snapshot.key} added")
                    markers.value += ImageMarker(skiLiftJSON.x, skiLiftJSON.y, mutableStateOf(skiLifts.last()))
                }
                callback()
            }
            catch (e: Exception) {
                Log.e("FirebaseService", "Error onChildAdded", e)
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            if (snapshot.value == null) {
                return
            }
            try {
                val skiLiftJSON = snapshot.getValue(SkiLiftJSON::class.java)
                if (skiLiftJSON != null) {
                    val skiLift = skiLifts.find { it.id == UUID.fromString(skiLiftJSON.id) }
                    if (skiLift != null) {
                        try {
                            skiLift.status.value = Status.valueOf(skiLiftJSON.status)
                        } catch (e: Exception) {
                            skiLift.status.value = Status.UNKNOWN
                            Log.e("FirebaseService", "Error parsing Status", e)
                        }
                        skiLift.comments = commentsHashMapToList(skiLiftJSON.comments)
                        skiLift.marker?.x = skiLift.x
                        skiLift.marker?.y = skiLift.y
                        skiLift.marker?.data?.value = skiLift
                        skiLift.hopIds = skiLiftJSON.hop.map { UUID.fromString(it)}
                    }
                }
                Log.d("FirebaseService", "SkiLift ${snapshot.key} changed")
            }
            catch (e: Exception) {
                Log.e("FirebaseService", "Error onChildChanged", e)
            }
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

    class TrackListener(private val tracks: SnapshotStateList<Track>, private val markers: MutableStateFlow<List<ImageMarker>>, private val callback: () -> Unit) : ChildEventListener,
        CommentsHashMapToList() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            if (snapshot.value == null) {
                return
            }
            try {
                val trackJSON = snapshot.getValue(TrackJSON::class.java)
                if (trackJSON != null) {
                    tracks.add(
                        Track(
                            id = UUID.fromString(trackJSON.id),
                            name = trackJSON.name,
                            comments = commentsHashMapToList(trackJSON.comments),
                            difficulty = Difficulty.valueOf(trackJSON.difficulty),
                            section = trackJSON.section,
                            status = try { mutableStateOf(Status.valueOf(trackJSON.status)) } catch (e: Exception) {
                                Log.e("FirebaseService", "Error parsing Status", e)
                                mutableStateOf(Status.UNKNOWN)
                            },
                            hop = mutableListOf(),
                            x = trackJSON.x,
                            y = trackJSON.y,
                            hopIds = trackJSON.hop.map { UUID.fromString(it) }
                        )
                    )
                    Log.d("FirebaseService", "Track ${snapshot.key} added")
                    markers.value += ImageMarker(trackJSON.x, trackJSON.y, mutableStateOf(tracks.last()))
                    callback()
                }
            }
            catch (e: Exception) {
                Log.e("FirebaseService", "Error onChildAdded", e)
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            if (snapshot.value == null) {
                return
            }
            try {
                val trackJSON = snapshot.getValue(TrackJSON::class.java)
                if (trackJSON != null) {
                    val track = tracks.find { it.id == UUID.fromString(trackJSON.id) }
                    if (track != null) {
                        try {
                            track.status.value = Status.valueOf(trackJSON.status)
                        } catch (e: Exception) {
                            track.status.value = Status.UNKNOWN
                            Log.e("FirebaseService", "Error parsing Status", e)
                        }
                        track.comments = commentsHashMapToList(trackJSON.comments)
                        track.marker?.x = track.x
                        track.marker?.y = track.y
                        track.marker?.data?.value = track
                        track.hopIds = trackJSON.hop.map { UUID.fromString(it) }
                    }
                }
                Log.d("FirebaseService", "Track ${snapshot.key} changed")
            }
            catch (e: Exception) {
                Log.e("FirebaseService", "Error onChildChanged", e)
            }
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

    class ChatMessagesListener(private val chatMessages: SnapshotStateList<ChatMessage>) : ChildEventListener {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            if (snapshot.value == null) {
                return
            }
            if (snapshot.key == null) {
                return
            }
            try {
                val chatMessageJSON = snapshot.getValue(ChatMessageJSON::class.java)
                if (chatMessageJSON != null) {
                    Log.d("FirebaseService", "{{$snapshot}}")
                    chatMessages.add(
                        ChatMessage(
                            snapshot.key!!,
                            chatMessageJSON
                        )
                    )
                }
            }
            catch (e: Exception) {
                Log.e("FirebaseService", "Error onChildAdded", e)
            }
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            TODO("Not yet implemented")
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            chatMessages.removeIf { it.id == snapshot.key }
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            TODO("Not yet implemented")
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("FirebaseService", "Error onChildAdded", error.toException())
        }
    }

    private fun fetchAllHops() {
        val allPath = this.tracks + this.skiLifts
        for (path in allPath) {
            path.hop = allPath.filter { it.id in path.hopIds }
        }
    }
}

abstract class CommentsHashMapToList {
    public fun commentsHashMapToList(comments: HashMap<String, CommentJSON>): SnapshotStateList<Comment> {
        val list = SnapshotStateList<Comment>()
        for (comment in comments) {
            list.add(Comment(comment.key, comment.value))
        }
        return list
    }
}