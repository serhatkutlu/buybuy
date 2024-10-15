package com.example.buybuy.data.repository

import com.example.buybuy.data.model.data.Address
import com.example.buybuy.domain.repository.AddressRepository
import com.example.buybuy.util.Constant
import com.example.buybuy.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait
import javax.inject.Inject

class AddressRepositoryImp @Inject constructor(
    private val firestore: FirebaseFirestore, private val authentication: FirebaseAuth,
) : AddressRepository {

    private val uid = authentication.currentUser?.uid.toString()



    override suspend fun  saveAddress(address: Address): Flow<Resource<Nothing>> = flow {
        emit(Resource.Loading())
        try {
            firestore.collection(Constant.COLLECTION_PATH_ADDRESS).document(uid)
                .collection(Constant.COLLECTION_PATH_ADDRESS_NAME)
                .document(address.addressName).set(address)
                .await()
            emit(Resource.Success())
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    override suspend fun getAllAddress(): Flow<Resource<List<Address>>> = flow {
        emit(Resource.Loading())
        try {
            val snapshot = firestore.collection(Constant.COLLECTION_PATH_ADDRESS).document(uid)
                .collection(Constant.COLLECTION_PATH_ADDRESS_NAME).get().await()
            snapshot.toObjects(Address::class.java).let {
                emit(Resource.Success(it))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))

            }

    }

    override suspend fun deleteAddress(addressId: String): Flow<Resource<Nothing>> {
        TODO("Not yet implemented")
    }
}