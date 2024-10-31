package com.example.buybuy.data.repository

import com.example.buybuy.data.model.data.OrderData
import com.example.buybuy.data.model.data.ProductDetail
import com.example.buybuy.domain.datasource.local.FlashSaleDataSource
import com.example.buybuy.domain.datasource.remote.RemoteDataSource
import com.example.buybuy.domain.repository.OrdersRepository
import com.example.buybuy.util.Constant
import com.example.buybuy.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class OrdersRepositoryImp(
    private val firestore: FirebaseFirestore,
    authentication: FirebaseAuth,
    private val dataSource: RemoteDataSource
) :
    OrdersRepository {

    private val uid = authentication.currentUser?.uid.toString()

    override suspend fun saveOrder(order: List<OrderData>): Boolean {
        return try {
            val batch = firestore.batch()

            val ordersCollection = firestore.collection(Constant.COLLECTION_PATH_ORDERS)
                .document(uid)
                .collection("user_orders")

            order.forEach { orderItem ->
                val docRef = ordersCollection.document()
                batch.set(docRef, orderItem)
            }
            batch.commit().await()
             true
        } catch (e: Exception) {
             false
        }
    }

    override suspend fun getAllOrder(): Resource<List<OrderData>> {
        var list = mutableListOf<OrderData>()
        try {
            val snapshot=firestore.collection(Constant.COLLECTION_PATH_ORDERS).document(uid)
                .collection("user_orders").get().await()

//                    it.documents().map
//                list = it.toObjects(OrderData::class.java)
//            }
            snapshot.documents.forEach {
                list+=it.toObject(OrderData::class.java)!!
            }
            return Resource.Success(list)
        } catch (e: Exception) {
            return Resource.Error(e.message.toString())
        }
    }

    override suspend fun getProductDetail(id: List<Int>): List<ProductDetail> {
        val list = mutableListOf<ProductDetail>()
        id.forEach {
            val response=dataSource.getSingleProduct(it)
            if(response.isSuccessful){
                response.body()?.let{
                    list.add(it.product)
                }
            }
        }
        return list
    }


}

