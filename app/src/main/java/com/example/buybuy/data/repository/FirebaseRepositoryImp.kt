package com.example.buybuy.data.repository

import android.util.Log
import com.example.buybuy.data.model.data.UserData
import com.example.buybuy.domain.model.User
import com.example.buybuy.domain.repository.FirebaseRepository
import com.example.buybuy.util.Constant.COLLECTION_PATH
import com.example.buybuy.util.Constant.EMAIL
import com.example.buybuy.util.Constant.ID
import com.example.buybuy.util.Constant.IMAGE
import com.example.buybuy.util.Constant.IMAGES
import com.example.buybuy.util.Constant.NAME
import com.example.buybuy.util.Constant.USER_NOT_FOUND
import com.example.buybuy.util.Resource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

import javax.inject.Inject

class FirebaseRepositoryImp @Inject constructor(
    private val authentication: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : FirebaseRepository {
    override fun createUser(user: User): Flow<Resource<Nothing>> = flow {
        try {
            emit(Resource.Loading())

            var imageUrl = ""
            val authResult =
                authentication.createUserWithEmailAndPassword(user.email, user.password ?: "")
                    .await()
            val uid = authResult.user?.uid.toString()
            user.image?.let {
                val storageRef = storage.reference.child(IMAGES)

                val imageRef = storageRef.child(user.email)
                imageRef.putFile(it).await()

                val downloadUri = imageRef.downloadUrl.await()
                imageUrl = downloadUri.toString()

            }
            val userModel = hashMapOf(
                ID to uid,
                EMAIL to user.email,
                NAME to user.name,
                IMAGE to imageUrl,
            )
            firestore.collection(COLLECTION_PATH).document(uid)
                .set(userModel).await()
            emit(Resource.Success())


        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun getUserData(): Flow<Resource<UserData>> {
        return flow {
            try {
                emit(Resource.Loading())
                val uid = authentication.currentUser?.uid.toString()
                val snapshot = firestore.collection(COLLECTION_PATH).document(uid).get().await()
                val user = snapshot.toObject(UserData::class.java)
                if (user != null) {
                    emit(Resource.Success(user))
                } else {
                    emit(Resource.Error(USER_NOT_FOUND))
                }
            } catch (e: Exception) {
                Log.d("serhat", "initObserver: ${e.message.toString()}")
                emit(Resource.Error(e.message.toString()))
            }

        }
    }
    override fun checkUserLogin() = flow {
        emit(authentication.currentUser != null)

    }

    override fun checkUserEmailAndPassword(email: String, password: String) = flow {
        emit(Resource.Loading())

        val user = authentication.signInWithEmailAndPassword(email, password).await()
        if (user != null) {
            emit(Resource.Success())
        } else {
            emit(Resource.Error(USER_NOT_FOUND))
        }


    }

    override fun resetPassword(email: String): Flow<Resource<Nothing>> = callbackFlow {
        authentication.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                trySend(Resource.Success())
            } else {
                trySend(Resource.Error(task.exception.toString()))
            }
            close()
        }
        awaitClose {}
    }

    override fun logOut(): Flow<Resource<Nothing>> = flow {
        emit(Resource.Loading())
        try {
            authentication.signOut()

            emit(Resource.Success())
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }


    }


}