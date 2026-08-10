package com.iti.mongez.data.repositories.auth.repository

import com.iti.mongez.data.mapper.toDomain
import com.iti.mongez.data.mapper.toEntity
import com.iti.mongez.data.dtos.HandshakeRequestDto
import com.iti.mongez.data.local.dao.UserDao
import com.iti.mongez.data.sources.local.TokenManager
import com.iti.mongez.data.sources.remote.services.ApiService
import com.iti.mongez.data.network.safeApi
import com.iti.mongez.data.sources.remote.FirebaseAuthDataSource
import com.iti.mongez.domain.auth.model.User
import com.iti.mongez.domain.auth.repository.AuthRepository
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.settings.repository.AppSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.firstOrNull
import java.util.Locale.getDefault
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuthDataSource: FirebaseAuthDataSource,
    private val apiService: ApiService,
    private val tokenManager: TokenManager,
    private val userDao: UserDao,
    private val appSettingsRepository: AppSettingsRepository
) : AuthRepository {

    private suspend fun buildHandshakeRequest(
        providedName: String? = null,
    ): HandshakeRequestDto {
        val settings = appSettingsRepository.getAppSettings().firstOrNull()
        val appearance = if (settings?.isDarkModeEnabled == true) "Dark Mode" else "Light Mode"
        val languageCode = settings?.language?.code
        val finalLanguage = if (languageCode == "system") {
            getDefault().language.let { if (it.startsWith("ar")) "ar" else "en" }
        } else {
            languageCode ?: "en"
        }
        val name = providedName ?: firebaseAuthDataSource.getCurrentUserName() ?: ""
        
        return HandshakeRequestDto(
            name = name,
            appearance = appearance,
            language = finalLanguage
        )
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return safeApi {
            val firebaseToken = firebaseAuthDataSource.signInWithEmail(email, password)
            tokenManager.saveToken(firebaseToken)
            println("Firebase Token : $firebaseToken")
            
            val profile = apiService.getFullUserProfile()
            val request = buildHandshakeRequest(profile.name)
            
            val response = apiService.handshake(request)
            val user = response.toDomain()
            userDao.insertUser(user.toEntity())
            user
        }
    }

    override suspend fun register(firstName: String, email: String, password: String): Result<User> {
        return safeApi {
            val firebaseToken = firebaseAuthDataSource.createUserWithEmail(email, password)
            tokenManager.saveToken(firebaseToken)
            
            // Sync name with Firebase immediately
            firebaseAuthDataSource.updateDisplayName(firstName)
            
            println("Firebase Token : $firebaseToken")
            val request = buildHandshakeRequest(firstName)
            val response = apiService.handshake(request)
            val user = response.toDomain()
            userDao.insertUser(user.toEntity())
            user
        }
    }

    override suspend fun loginWithGoogle(idToken: String): Result<User> {
        return safeApi {
            val firebaseToken = firebaseAuthDataSource.signInWithGoogleCredential(idToken)
            tokenManager.saveToken(firebaseToken)

            val request = buildHandshakeRequest()

            val response = apiService.handshake(request)
            val user = response.toDomain()
            userDao.insertUser(user.toEntity())
            user
        }
    }

    override suspend fun hasToken(): Boolean {
        val token = tokenManager.tokenFlow.firstOrNull()
        return !token.isNullOrEmpty()
    }

    override suspend fun logout() {
        firebaseAuthDataSource.logout()
        userDao.clearUser()
        tokenManager.clearToken()
    }

    override suspend fun getCurrentUser(): User? {
        return userDao.getUser()?.toDomain()
    }

    override fun getCurrentUserFlow(): Flow<User?> {
        return userDao.getUserFlow().map { it?.toDomain() }
    }
}
