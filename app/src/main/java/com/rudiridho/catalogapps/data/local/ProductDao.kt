package com.rudiridho.catalogapps.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rudiridho.catalogapps.presentation.model.ProductUI
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductUI>)

    @Query("SELECT * FROM productui")
    fun getAllProducts(): Flow<List<ProductUI>>

    @Query("SELECT * FROM productui WHERE isFavorite = 1")
    fun getFavoriteProducts(): Flow<List<ProductUI>>

    @Query("SELECT * FROM productui WHERE name LIKE '%' || :query || '%'")
    fun searchProducts(query: String): Flow<List<ProductUI>>

    @Update
    suspend fun updateProduct(product: ProductUI)
}