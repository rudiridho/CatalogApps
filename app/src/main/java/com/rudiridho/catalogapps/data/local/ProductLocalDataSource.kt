package com.rudiridho.catalogapps.data.local

import com.rudiridho.catalogapps.presentation.model.ProductUI
import kotlinx.coroutines.flow.Flow

interface ProductLocalDataSource {
    suspend fun insertProducts(products: List<ProductUI>)
    fun getFavoriteProducts(): Flow<List<ProductUI>>
    fun searchProducts(query: String): Flow<List<ProductUI>>
    suspend fun updateProduct(product: ProductUI)
}

class ProductLocalDataSourceImpl(private val productDao: ProductDao) : ProductLocalDataSource {
    override suspend fun insertProducts(products: List<ProductUI>) =
        productDao.insertProducts(products)

    override fun getFavoriteProducts(): Flow<List<ProductUI>> = productDao.getFavoriteProducts()

    override fun searchProducts(query: String): Flow<List<ProductUI>> =
        productDao.searchProducts(query)

    override suspend fun updateProduct(product: ProductUI) = productDao.updateProduct(product)
}