package com.example.buybuy.data.repository

import com.example.buybuy.data.model.data.User
import com.example.buybuy.domain.repository.Firebase
import com.example.buybuy.util.Constant.COLLECTION_PATH
import com.example.buybuy.util.Constant.EMAIL
import com.example.buybuy.util.Constant.ID
import com.example.buybuy.util.Constant.IMAGE
import com.example.buybuy.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuth @Inject constructor (
        private val authentication: FirebaseAuth,
        private val firestore: FirebaseFirestore,
        private val storage: FirebaseStorage
) : Firebase {
    override suspend fun createUser(user: User):Flow<Resource<Nothing>> = flow {
        try {
            emit(Resource.Loading())

            var imageUrl=""
            val authResult = authentication.createUserWithEmailAndPassword(user.email, user.password).await()
            val uid = authResult.user?.uid.toString()
            user.image?.let{
                val storageRef = storage.reference.child("images")

                val imageRef = storageRef.child(user.email)
                imageRef.putFile(it).await()

                val downloadUri = imageRef.downloadUrl.await()
                imageUrl = downloadUri.toString()

            }
            val userModel = hashMapOf(
                ID to uid,
                EMAIL to user.email,
                IMAGE to imageUrl,
            )
            firestore.collection(COLLECTION_PATH).document(uid)
                .set(userModel).await()
            emit(Resource.Success())


        }catch (e:Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }


}