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


}