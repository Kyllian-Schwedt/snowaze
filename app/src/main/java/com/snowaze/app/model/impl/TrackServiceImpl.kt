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
                    ref.child("chatMessages").addChildEventListener(ChatMessagesListener(this.chatMessages))
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

    override fun updateSkiLiftStatus(id: UUID, status: Status) {
        this.database.getReference("skiLifts").child(id.toString()).child("status").setValue(status.toString())
    }

    override fun addCommentToTrack(id: UUID, text:String, author: String) {
        val comment = Comment(author, text)
        this.database.getReference("tracks").child(id.toString()).child("comments").push().setValue(comment.toJson())
    }

    override fun addCommentToSkiLift(id: UUID, text:String, author: String) {
        val comment = Comment(author, text)
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
        paths = paths.filter { it.all { it is Track && it.status.value == Status.OPEN || it is SkiLift && it.status == Status.OPEN } }.toMutableList()
        // Take the 5 shortest path
        paths.sortBy { it.size }
        if (paths.size > 5) {
            paths = paths.subList(0, 5)
        }

        return paths
    }

    class SkiLiftListener(private val skiLifts: SnapshotStateList<SkiLift>) : ChildEventListener, CommentsHashMapToList() {
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
                            status = try { Status.valueOf(skiLiftJSON.status) } catch (e: Exception) {
                                Log.e("FirebaseService", "Error parsing Status", e)
                                Status.UNKNOWN
                            },
                            hop = mutableListOf()
                        )
                    )
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
                val skiLiftJSON = snapshot.getValue(SkiLiftJSON::class.java)
                if (skiLiftJSON != null) {
                    val skiLift = skiLifts.find { it.id == UUID.fromString(skiLiftJSON.id) }
                    if (skiLift != null) {
                        try {
                            skiLift.status = Status.valueOf(skiLiftJSON.status)
                        } catch (e: Exception) {
                            skiLift.status = Status.UNKNOWN
                            Log.e("FirebaseService", "Error parsing Status", e)
                        }
                        skiLift.comments = commentsHashMapToList(skiLiftJSON.comments)
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

    class TrackListener(private val tracks: SnapshotStateList<Track>) : ChildEventListener,
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
                            hop = mutableListOf()
                        )
                    )
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
}

abstract class CommentsHashMapToList {
    public fun commentsHashMapToList(comments: HashMap<String, CommentJSON>): List<Comment> {
        val list = mutableListOf<Comment>()
        for (comment in comments) {
            list.add(Comment(comment.key, comment.value))
        }
        return list
    }
}