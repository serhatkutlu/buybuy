package com.example.buybuy.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.buybuy.data.model.data.ProductDetail

@Dao
interface SearchDAO {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(Products: ProductDetail)

    @Query("SELECT * FROM ProductDetail where LOWER(title)  LIKE '%' || :query || '%'")
    fun searchAll(query: String): List<ProductDetail>?


    @Query("UPDATE ProductDetail SET isFavorite = 0 WHERE id = :productId")
    fun deleteFromFavorite(productId:Int)

    @Query("UPDATE ProductDetail SET isFavorite = 1 WHERE id = :productId")
    fun addToFavorite(productId:Int)




    @Query("SELECT * FROM productdetail WHERE isFavorite = 1")
    fun getAllFavoriteProducts():List<ProductDetail>
    @Query("SELECT EXISTS (SELECT 1 FROM productdetail WHERE id = :productId AND isFavorite = 1)")
    fun isProductFavorite(productId: Int): Boolean



}