package com.luis2576.dev.rickandmorty.features.authentication.data.repository

import android.content.Context
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.luis2576.dev.rickandmorty.R
import com.luis2576.dev.rickandmorty.features.authentication.data.responses.LoginResponse
import com.luis2576.dev.rickandmorty.features.authentication.data.responses.LogoutResponse
import com.luis2576.dev.rickandmorty.features.authentication.data.responses.RegistrationResponse
import com.luis2576.dev.rickandmorty.features.authentication.data.responses.ResetPasswordResponse
import com.luis2576.dev.rickandmorty.features.authentication.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val context: Context,
    private val auth: FirebaseAuth
) : AuthRepository {

    override suspend fun isUserLoggedIn(): LoginResponse<FirebaseUser> {
        return try {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                LoginResponse.Logged(currentUser)
            } else {
                LoginResponse.UnLogged
            }
        } catch (e: Exception) {
            LoginResponse.Failure(e)
        }
    }

    override suspend fun loginUser(email: String, password: String): LoginResponse<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.let {
                LoginResponse.Logged(it)
            } ?: LoginResponse.UnLogged
        } catch (e: FirebaseAuthException) {
            handleFirebaseAuthException(e)
        } catch (e: FirebaseTooManyRequestsException) {
            LoginResponse.TooManyRequestsError(e)
        } catch (e: IllegalArgumentException) {
            LoginResponse.Failure(e)
        } catch (e: Exception) {
            LoginResponse.Failure(e)
        }
    }

    override suspend fun registerUser(email: String, password: String): RegistrationResponse<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let { firebaseUser ->
                RegistrationResponse.Registered(firebaseUser)
            } ?: RegistrationResponse.UnRegistered
        } catch (e: FirebaseAuthException) {
            handleFirebaseAuthRegistrationException(e)
        } catch (e: Exception) {
            RegistrationResponse.Failure(e)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): ResetPasswordResponse<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            ResetPasswordResponse.PasswordResetEmailSent(Unit)
        } catch (e: FirebaseAuthInvalidUserException) {
            when (e.errorCode) {
                "ERROR_INVALID_EMAIL" -> ResetPasswordResponse.EmailError(Exception(context.getString(R.string.error_email_invalid)))
                "ERROR_USER_NOT_FOUND" -> ResetPasswordResponse.UserNotFoundError(Exception(context.getString(R.string.error_user_not_found)))
                else -> ResetPasswordResponse.Failure(e)
            }
        } catch (e: Exception) {
            ResetPasswordResponse.Failure(e)
        }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun logoutUser(): LogoutResponse<Unit> {
        return try {
            auth.signOut()
            LogoutResponse.Success(Unit)
        } catch (e: Exception) {
            LogoutResponse.Error(e)
        }
    }

    private fun handleFirebaseAuthRegistrationException(e: FirebaseAuthException): RegistrationResponse<FirebaseUser> {
        return when (e.errorCode) {
            "ERROR_EMAIL_ALREADY_IN_USE" -> RegistrationResponse.EmailAlreadyInUseError(Exception(context.getString(R.string.error_email_already_in_use)))
            "ERROR_WEAK_PASSWORD" -> RegistrationResponse.WeakPasswordError(Exception(context.getString(R.string.error_weak_password)))
            "ERROR_INVALID_EMAIL" -> RegistrationResponse.InvalidCredentialsError(Exception(context.getString(R.string.error_invalid_email)))
            else -> RegistrationResponse.Failure(e)
        }
    }

    private fun handleFirebaseAuthException(e: FirebaseAuthException): LoginResponse<FirebaseUser> {
        return when (e.errorCode) {
            "ERROR_INVALID_EMAIL" -> LoginResponse.EmailError(Exception(context.getString(R.string.error_invalid_email)))
            "ERROR_USER_NOT_FOUND" -> LoginResponse.UserNotFoundError(Exception(context.getString(R.string.error_user_not_found)))
            "ERROR_WRONG_PASSWORD" -> LoginResponse.PasswordError(Exception(context.getString(R.string.error_wrong_password)))
            else -> LoginResponse.Failure(e)
        }
    }
}