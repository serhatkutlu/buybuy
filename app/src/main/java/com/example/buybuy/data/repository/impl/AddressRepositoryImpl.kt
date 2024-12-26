package com.example.buybuy.data.repository.impl

import com.example.buybuy.data.model.data.AddressData
import com.example.buybuy.data.repository.base.BaseRepository
import com.example.buybuy.domain.repository.AddressRepository
import com.example.buybuy.util.Constant
import com.example.buybuy.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore, authentication: FirebaseAuth,
) : AddressRepository, BaseRepository(Dispatchers.IO) {

    private val uid = authentication.currentUser?.uid.toString()


    override suspend fun saveAddress(addressData: AddressData): Flow<Resource<Unit>> =
        safeCall{
            firestore.collection(Constant.COLLECTION_PATH_ADDRESS).document(uid)
                .collection(Constant.COLLECTION_PATH_ADDRESS_NAME)
                .document().set(addressData)
                .await()
        }


    override suspend fun getAllAddress(): Flow<Resource<List<AddressData>>> =
        safeCall{
            val snapshot = firestore.collection(Constant.COLLECTION_PATH_ADDRESS).document(uid)
                .collection(Constant.COLLECTION_PATH_ADDRESS_NAME).get().await()
             snapshot.documents.mapNotNull {
                it.toObject(AddressData::class.java)?.copy(id = it.id)
            }

        }


    override suspend fun deleteAddress(addressId: String): Flow<Resource<Unit>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateAddress(
        addressData: AddressData,
        id: String
    ): Flow<Resource<Unit>> =
        safeCall{
            firestore.collection(Constant.COLLECTION_PATH_ADDRESS).document(uid)
                .collection(Constant.COLLECTION_PATH_ADDRESS_NAME).document(id).set(addressData)
                .await()
            Unit
        }

}