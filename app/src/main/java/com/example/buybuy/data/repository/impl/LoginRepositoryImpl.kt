package com.example.buybuy.data.repository.impl

import com.example.buybuy.data.repository.base.BaseRepository
import com.example.buybuy.domain.model.data.UserData
import com.example.buybuy.domain.model.data.User
import com.example.buybuy.domain.repository.LoginRepository
import com.example.buybuy.util.Constant.COLLECTION_PATH_USERS
import com.example.buybuy.util.Constant.EMAIL
import com.example.buybuy.util.Constant.ID
import com.example.buybuy.util.Constant.IMAGE
import com.example.buybuy.util.Constant.IMAGES
import com.example.buybuy.util.Constant.NAME
import com.example.buybuy.util.Constant.USER_NOT_FOUND
import com.example.buybuy.util.Resource
import com.example.buybuy.util.Resource.Empty.transform

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LoginRepositoryImpl @Inject constructor(
    private val authentication: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : LoginRepository, BaseRepository(Dispatchers.IO) {
    override suspend fun createUser(user: User): Flow<Resource<Unit>> =
        safeCall {
            var imageUrl = ""
            val authResult =
                authentication.createUserWithEmailAndPassword(user.email, user.password)
                    .await()
            val uid = authResult.user?.uid.toString()
            user.image?.let {
                try {
                    val storageRef = storage.reference.child(IMAGES)

                    val imageRef = storageRef.child(user.email)
                    imageRef.putFile(it).await()

                    val downloadUri = imageRef.downloadUrl.await()
                    imageUrl = downloadUri.toString()

                } catch (e: Exception) {
                }
            }
            val userModel = hashMapOf(
                ID to uid,
                EMAIL to user.email,
                NAME to user.name,
                IMAGE to imageUrl,
            )
            firestore.collection(COLLECTION_PATH_USERS).document(uid)
                .set(userModel).await()
        }


    override suspend fun getUserData(): Flow<Resource<UserData>> =
        safeCall {
            val uid = authentication.currentUser?.uid.toString()
            val snapshot = firestore.collection(COLLECTION_PATH_USERS).document(uid).get().await()
            snapshot.toObject(UserData::class.java)

        }.map {
            it.transform {
                if (it == null) Resource.Error(USER_NOT_FOUND)
                else Resource.Success(it)
            }

        }


    override suspend fun checkUserLogin(): Flow<Resource<Boolean>> =
        safeCall {
            authentication.currentUser?.uid != null
        }


    override suspend fun checkUserEmailAndPassword(email: String, password: String) =
        safeCall {
            authentication.signInWithEmailAndPassword(email, password).await()
            Unit
        }

    override suspend fun resetPassword(email: String): Flow<Resource<Unit>> =
        safeCall {
            suspendCoroutine<Result<Unit>> { continuation ->
                authentication.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(Result.success(Unit))
                        } else {
                            continuation.resume(
                                Result.failure(
                                    task.exception ?: Exception("Unknown error")
                                )
                            )
                        }
                    }
            }

        }.map {
            it.transform {
                it?.let {
                    if (it.isSuccess) Resource.Success(Unit)
                    else Resource.Error(it.exceptionOrNull()?.localizedMessage ?: "Unknown error")

                } ?: Resource.Error("")
            }
        }


    override suspend fun logOut(): Flow<Resource<Unit>> =
        safeCall {
            authentication.signOut()
        }


}
