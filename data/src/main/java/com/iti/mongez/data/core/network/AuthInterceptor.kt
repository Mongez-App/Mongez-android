package com.iti.mongez.data.core.network

import com.iti.mongez.data.core.local.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.util.Locale
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        // 1. Fetch token from DataStore synchronously on this background thread
        val token = runBlocking {
            tokenManager.tokenFlow.first()
        }

        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        // 2. Handle Accept-Language Header (en or ar)
        val currentLanguage = Locale.getDefault().language
        val apiLanguage = if (currentLanguage == "ar") "ar" else "en"
        requestBuilder.addHeader("Accept-Language", apiLanguage)

        // 3. Enforce JSON payloads
        requestBuilder.addHeader("Accept", "application/json")

        return chain.proceed(requestBuilder.build())
    }
}