package com.iti.mongez.data.sources.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun signInWithEmail(email: String, password: String): String {
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return result.user?.getIdToken(true)?.await()?.token ?: throw Exception("Token retrieval failed")
    }

    suspend fun createUserWithEmail(email: String, password: String): String {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        return result.user?.getIdToken(true)?.await()?.token ?: throw Exception("Token retrieval failed")
    }

    suspend fun signInWithGoogleCredential(idToken: String): String {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = firebaseAuth.signInWithCredential(credential).await()
        return result.user?.getIdToken(true)?.await()?.token ?: throw Exception("Token retrieval failed")
    }

    fun logout() = firebaseAuth.signOut()

    fun getCurrentUserName(): String? = firebaseAuth.currentUser?.displayName
}