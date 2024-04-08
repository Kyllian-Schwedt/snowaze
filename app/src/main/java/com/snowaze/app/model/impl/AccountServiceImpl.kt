package com.snowaze.app.model.impl

import android.os.Trace
import android.util.Log
import androidx.core.os.trace
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.snowaze.app.model.AccountService
import com.snowaze.app.model.User
import com.snowaze.app.model.UserDetail
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AccountServiceImpl @Inject constructor(private val auth: FirebaseAuth, private val store: FirebaseFirestore) : AccountService {

    private val userDetailCache = mutableMapOf<String, UserDetail?>()
    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override val hasUser: Boolean
        get() = auth.currentUser != null

    override val fullUser: Flow<UserDetail?>
        get() = callbackFlow {
            val listener = store.collection("users").document(currentUserId)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        // Log the error
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        val userDetail = snapshot.toObject(UserDetail::class.java)
                        this@AccountServiceImpl.syncFullUser = userDetail
                        trySend(userDetail)
                    } else {
                        trySend(null)
                    }
                }

            awaitClose { listener.remove() }
        }

    override var syncFullUser: UserDetail? = null

    override val currentUser: Flow<User>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser?.let { User(it.uid, it.isAnonymous) } ?: User())
                }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    override suspend fun authenticate(email: String, password: String) {
            auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun sendRecoveryEmail(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    override suspend fun createAnonymousAccount() {
        auth.signInAnonymously().await()
    }

    override suspend fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
        authenticate(email, password)
    }

    override suspend fun linkAccount(email: String, password: String): Unit {
        Trace.beginSection(LINK_ACCOUNT_TRACE)
        try {
            if(auth.currentUser == null) {
                register(email, password)
            } else {
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    val credential = EmailAuthProvider.getCredential(email, password)
                    currentUser.linkWithCredential(credential).await()
                } else {
                    throw IllegalStateException("Current user is null")
                }
            }
        } finally {
            Trace.endSection()
        }
    }

    override suspend fun deleteAccount() {
        auth.currentUser!!.delete().await()
    }

    override suspend fun signOut() {
        val currentUser = auth.currentUser
        if (currentUser != null && currentUser.isAnonymous) {
            currentUser.delete()
        }

        auth.signOut()

        // Sign the user back in anonymously.
        createAnonymousAccount()
    }

    //Store user data in Firestore
    override suspend fun storeUserData(userDetail: UserDetail) {
        Log.d("AccountServiceImpl", "setUserData : $currentUserId")
        store.collection("users").document(currentUserId).set(userDetail).await()

    }

    //Get user data from Firestore
    override suspend fun getUserData(): UserDetail? {
        Log.d("AccountServiceImpl", "getUserData : $currentUserId")
        return store.collection("users").document(currentUserId).get().await().toObject(UserDetail::class.java)
    }

    override suspend fun googleSignIn(idToken: String): Unit {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).await()
    }

    override fun getGso(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken("552161327479-5ac2rc45lcv65sn5lmpb2vdsa7mcnvmo.apps.googleusercontent.com")
            .build()
    }


    companion object {
        private const val LINK_ACCOUNT_TRACE = "linkAccount"
    }

    override suspend fun getAccountInfoById(id: String): UserDetail? {
        return userDetailCache.getOrPut(id) {
            store.collection("users").document(id).get().await().toObject(UserDetail::class.java)
        }
    }

    override suspend fun getAccountsInfoByIds(ids: List<String>): List<UserDetail> {
        return store.collection("users").whereIn("id", ids).get().await().toObjects(UserDetail::class.java)
    }

    override fun disconnect() {
        auth.signOut()
    }
}