package com.example.buybuy.data.repository

import com.example.buybuy.data.model.data.CouponData
import com.example.buybuy.domain.repository.CouponRepository
import com.example.buybuy.util.Constant
import com.example.buybuy.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class CouponRepositoryImp @Inject constructor(
    authentication: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : CouponRepository {

    private val uid = authentication.currentUser?.uid
    override suspend fun createRegisterCoupon(couponData: CouponData): Flow<Resource<Nothing>> = flow {
        try {
            if (uid == null) emit(Resource.Error(Constant.UNKNOWN_ERROR))
            else {
                firestore.collection(Constant.COLLECTION_PATH_COUPON).document(uid.toString()).set(couponData).await()
                emit(Resource.Success())
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: Constant.UNKNOWN_ERROR))
        }
    }

    override suspend fun getAllCoupon(): Flow<Resource<CouponData>> {
        TODO("Not yet implemented")
    }


}