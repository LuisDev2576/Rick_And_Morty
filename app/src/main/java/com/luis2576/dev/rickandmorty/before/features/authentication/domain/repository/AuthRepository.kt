package com.luis2576.dev.rickandmorty.before.features.authentication.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.luis2576.dev.rickandmorty.before.features.authentication.data.responses.LogoutResponse
import com.luis2576.dev.rickandmorty.before.features.authentication.data.responses.RegistrationResponse
import com.luis2576.dev.rickandmorty.before.features.authentication.data.responses.ResetPasswordResponse
import com.luis2576.dev.rickandmorty.before.features.authentication.data.responses.LoginResponse

interface AuthRepository {
    suspend fun loginUser(email: String, password: String): LoginResponse<FirebaseUser>
    suspend fun isUserLoggedIn(): LoginResponse<FirebaseUser>
    suspend fun registerUser(email: String, password: String): RegistrationResponse<FirebaseUser>
    suspend fun sendPasswordResetEmail(email: String): ResetPasswordResponse<Unit>
    fun getCurrentUser(): FirebaseUser?
    fun logoutUser(): LogoutResponse<Unit>
}