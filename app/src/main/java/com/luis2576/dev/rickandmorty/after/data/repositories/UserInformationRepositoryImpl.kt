package com.luis2576.dev.rickandmorty.after.data.repositories

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import com.luis2576.dev.rickandmorty.R
import com.luis2576.dev.rickandmorty.after.domain.models.responses.DownloadUserDataResponse
import com.luis2576.dev.rickandmorty.after.domain.models.responses.UploadUserDataResponse
import com.luis2576.dev.rickandmorty.after.domain.models.UserInformation
import com.luis2576.dev.rickandmorty.after.domain.repositories.UserInformationRepository
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInformationRepositoryImpl @Inject constructor(
    private val context: Context,
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage // Añade FirebaseStorage
) : UserInformationRepository {

    override fun downloadUserInformation(uid: String): Flow<DownloadUserDataResponse<UserInformation>> = callbackFlow {
        val documentReference = firebaseFirestore.collection("users").document(uid)
        val subscription = documentReference.addSnapshotListener { snapshot, e ->
            if (e != null) {
                trySend(DownloadUserDataResponse.Failure(e)).isSuccess
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val userInfo = snapshot.toObject(UserInformation::class.java)
                if (userInfo != null) {
                    trySend(DownloadUserDataResponse.UserInfoDownloaded(userInfo)).isSuccess
                } else {
                    val error = Exception(context.getString(R.string.error_interpreting_user_data))
                    trySend(DownloadUserDataResponse.Failure(error)).isSuccess
                }
            } else {
                val error = Exception(context.getString(R.string.error_user_data_not_found))
                trySend(DownloadUserDataResponse.Failure(error)).isSuccess
            }
        }
        awaitClose { subscription.remove() }
    }

    override suspend fun uploadUserInformation(uid: String, userInformation: UserInformation): UploadUserDataResponse {
        return try {
            Log.d("UploadUserInfo", "Intentando subir información del usuario con UID: $uid")
            withContext(NonCancellable) { // Evita que la coroutine sea cancelada
                firebaseFirestore.collection("users").document(uid).set(userInformation.copy(uid = uid)).await()
                // Retornar éxito inmediatamente después de subir la información básica
                UploadUserDataResponse.Success
            }
        } catch (e: FirebaseFirestoreException) {
            Log.e("UploadUserInfo", "FirebaseFirestoreException al subir la información del usuario con UID: $uid", e)
            when (e.code) {
                FirebaseFirestoreException.Code.PERMISSION_DENIED -> {
                    Log.e("UploadUserInfo", "Permisos insuficientes para subir la información del usuario con UID: $uid")
                    UploadUserDataResponse.Failure(
                        Exception(context.getString(R.string.error_insufficient_permissions))
                    )
                }
                else -> {
                    Log.e("UploadUserInfo", "Error desconocido al subir la información del usuario con UID: $uid", e)
                    UploadUserDataResponse.Failure(e)
                }
            }
        } catch (e: Exception) {
            Log.e("UploadUserInfo", "Excepción general al subir la información del usuario con UID: $uid", e)
            UploadUserDataResponse.Failure(e)
        }
    }
}

