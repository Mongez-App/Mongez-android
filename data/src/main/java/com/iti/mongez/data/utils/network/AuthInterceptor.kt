package com.iti.mongez.data.core.network


import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.util.Locale
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        // 1. Fetch token directly from Firebase (handles refresh automatically)
        val token = runBlocking {
            try {
                firebaseAuth.currentUser?.getIdToken(false)?.await()?.token
            } catch (e: Exception) {
                null
            }
        }

        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        // 2. Add X-User-Id Header
        firebaseAuth.currentUser?.uid?.let { uid ->
            requestBuilder.addHeader("X-User-Id", uid)
        }

        // 3. Handle Accept-Language Header (en or ar)
        val currentLanguage = Locale.getDefault().language
        val apiLanguage = if (currentLanguage == "ar") "ar" else "en"
        requestBuilder.addHeader("Accept-Language", apiLanguage)

        // 3. Enforce JSON payloads
        requestBuilder.addHeader("Accept", "application/json")

        return chain.proceed(requestBuilder.build())
    }
}