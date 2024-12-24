package com.example.buybuy.data.repository.Impl

import com.example.buybuy.data.model.data.CouponData
import com.example.buybuy.data.repository.base.BaseRepository
import com.example.buybuy.domain.repository.CouponRepository
import com.example.buybuy.util.Constant
import com.example.buybuy.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class CouponRepositoryImpl @Inject constructor(
    private val authentication: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : CouponRepository, BaseRepository(Dispatchers.IO) {


    override suspend fun createRegisterCoupon(couponData: List<CouponData>): Flow<Resource<Unit>> =
        safeCall{
            val uid = authentication.currentUser?.uid
            uid?.let{
                val userCouponsRef =
                    firestore.collection(Constant.COLLECTION_PATH_COUPON).document(uid)
                        .collection(Constant.COLLECTION_PATH_COUPON_USER)

                couponData.forEach {
                    userCouponsRef.document().set(it).await()
                }
            }
        }


    override suspend fun getAllCoupon(): Flow<Resource<List<CouponData>>> =
        safeCall{
            val list = mutableListOf<CouponData>()
            val uid = authentication.currentUser?.uid
            uid?.let{
                val snapshot =
                    firestore.collection(Constant.COLLECTION_PATH_COUPON).document(uid)
                        .collection(Constant.COLLECTION_PATH_COUPON_USER).get().await()

                snapshot.documents.forEach {
                    val couponData = it.toObject(CouponData::class.java)?.copy(id = it.id)
                    if (couponData != null) {
                        list.add(couponData)
                    }
                }

            }
            list
        }



     override suspend fun deactivateCoupon(id:String): Boolean{
         try {
             val uid = authentication.currentUser?.uid
             return uid?.let{
                 val userCouponsRef =
                     firestore.collection(Constant.COLLECTION_PATH_COUPON).document(uid)
                         .collection(Constant.COLLECTION_PATH_COUPON_USER)
                 userCouponsRef.document(id).update(Constant.COUPON_USED_FIELD_NAME,true).await()
                  true
             } ?: false
         }catch (e:Exception){
             return false
         }
     }

}