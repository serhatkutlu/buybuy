package com.example.buybuy.data.repository

import com.example.buybuy.data.model.data.AddressData
import com.example.buybuy.domain.repository.AddressRepository
import com.example.buybuy.util.Constant
import com.example.buybuy.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AddressRepositoryImp @Inject constructor(
    private val firestore: FirebaseFirestore, authentication: FirebaseAuth,
) : AddressRepository {

    private val uid = authentication.currentUser?.uid.toString()


    override suspend fun saveAddress(addressData: AddressData): Flow<Resource<Nothing>> = flow {
        emit(Resource.Loading())
        try {
            firestore.collection(Constant.COLLECTION_PATH_ADDRESS).document(uid)
                .collection(Constant.COLLECTION_PATH_ADDRESS_NAME)
                .document().set(addressData)
                .await()
            emit(Resource.Success())
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getAllAddress(): Flow<Resource<List<AddressData>>> = flow {
        emit(Resource.Loading())
        try {
            val snapshot = firestore.collection(Constant.COLLECTION_PATH_ADDRESS).document(uid)
                .collection(Constant.COLLECTION_PATH_ADDRESS_NAME).get().await()

            val address = snapshot.documents.mapNotNull {
                it.toObject(AddressData::class.java)?.copy(id = it.id)
            }
            emit(Resource.Success(address))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))

        }

    }.flowOn(Dispatchers.IO)

    override suspend fun deleteAddress(addressId: String): Flow<Resource<Nothing>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateAddress(
        addressData: AddressData,
        id: String
    ): Flow<Resource<Nothing>> = flow {
        emit(Resource.Loading())
        try {
            firestore.collection(Constant.COLLECTION_PATH_ADDRESS).document(uid)
                .collection(Constant.COLLECTION_PATH_ADDRESS_NAME).document(id).set(addressData)
                .await()

            emit(Resource.Success())
        }catch (e:Exception){
            emit(Resource.Error(e.message.toString()))

        }
    }
}