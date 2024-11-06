package com.example.buybuy.data.repository

import com.example.buybuy.data.model.data.CouponData
import com.example.buybuy.data.model.data.OrderData
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
    private val authentication: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : CouponRepository {


    override fun createRegisterCoupon(couponData: List<CouponData>): Flow<Resource<Nothing>> =
        flow {
            try {
                val uid = authentication.currentUser?.uid
                if (uid == null) emit(Resource.Error(Constant.UNKNOWN_ERROR))
                else {
                    val userCouponsRef =
                        firestore.collection(Constant.COLLECTION_PATH_COUPON).document(uid)
                            .collection(Constant.COLLECTION_PATH_COUPON_USER)

                    couponData.forEach {
                        userCouponsRef.document().set(it).await()
                    }

                    emit(Resource.Success())
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: Constant.UNKNOWN_ERROR))
            }
        }

    override fun getAllCoupon(): Flow<Resource<List<CouponData>>> = flow {
        val list = mutableListOf<CouponData>()
        val uid = authentication.currentUser?.uid

        emit(Resource.Loading())
        try {
            if (uid == null) emit(Resource.Error(Constant.UNKNOWN_ERROR))
            else {
                val snapshot =
                    firestore.collection(Constant.COLLECTION_PATH_COUPON).document(uid)
                        .collection(Constant.COLLECTION_PATH_COUPON_USER).get().await()

                snapshot.documents.forEach {
                    val couponData = it.toObject(CouponData::class.java)
                    if (couponData != null) {
                        list.add(couponData)
                    }
                }

                emit(Resource.Success(list))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: Constant.UNKNOWN_ERROR))


        }
    }


}