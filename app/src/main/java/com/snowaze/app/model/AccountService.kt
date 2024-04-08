package com.snowaze.app.model

import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean
    val fullUser: Flow<UserDetail?>

    val syncFullUser: UserDetail?

    val currentUser: Flow<User>


    suspend fun authenticate(email: String, password: String)
    suspend fun sendRecoveryEmail(email: String)
    suspend fun createAnonymousAccount()
    suspend fun register(email: String, password: String)
    suspend fun linkAccount(email: String, password: String)
    suspend fun deleteAccount()
    suspend fun signOut()
    suspend fun storeUserData(userDetail: UserDetail)
    suspend fun getUserData(): UserDetail?
    suspend fun googleSignIn(idToken: String)
    fun getGso(): GoogleSignInOptions

    suspend fun getAccountInfoById(id: String): UserDetail?
    suspend fun getAccountsInfoByIds(ids: List<String>): List<UserDetail>
    fun disconnect()
}