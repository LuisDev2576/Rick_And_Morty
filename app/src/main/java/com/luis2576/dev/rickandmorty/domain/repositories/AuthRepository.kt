package com.luis2576.dev.rickandmorty.domain.repositories

import com.google.firebase.auth.FirebaseUser
import com.luis2576.dev.rickandmorty.domain.models.responses.LogoutResponse
import com.luis2576.dev.rickandmorty.domain.models.responses.RegistrationResponse
import com.luis2576.dev.rickandmorty.domain.models.responses.ResetPasswordResponse
import com.luis2576.dev.rickandmorty.domain.models.responses.LoginResponse

interface AuthRepository {
    suspend fun loginUser(email: String, password: String): LoginResponse<FirebaseUser>
    suspend fun isUserLoggedIn(): LoginResponse<FirebaseUser>
    suspend fun registerUser(email: String, password: String): RegistrationResponse<FirebaseUser>
    suspend fun sendPasswordResetEmail(email: String): ResetPasswordResponse<Unit>
    fun getCurrentUser(): FirebaseUser?
    fun logoutUser(): LogoutResponse<Unit>
}