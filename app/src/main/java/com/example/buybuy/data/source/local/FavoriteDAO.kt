package com.example.buybuy.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.buybuy.data.model.data.ProductDetail


@Dao
interface FavoriteDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addToFavorite(product:ProductDetail)


    @Query("DELETE FROM productdetail WHERE id = :productId")
    fun deleteFromFavorite(productId:Int)

    @Query("SELECT * FROM productdetail")
    fun getFavoriteProducts(): List<ProductDetail>

    @Query("SELECT EXISTS (SELECT 1 FROM productdetail WHERE id = :productId)")
    fun isProductFavorite(productId: Int): Boolean


}