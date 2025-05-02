package com.rudiridho.catalogapps.domain.repository

import com.rudiridho.catalogapps.data.model.ProductResponse
import com.rudiridho.catalogapps.domain.model.ProductDomain
import com.rudiridho.catalogapps.presentation.model.ProductUI
import com.rudiridho.catalogapps.utils.DomainResult
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProducts():
            Flow<DomainResult<List<ProductDomain>>>
    suspend fun getFavoriteProducts():
            Flow<DomainResult<List<ProductDomain>>>
    suspend fun searchProducts(query: String):
            Flow<DomainResult<List<ProductDomain>>>
    suspend fun updateProduct(product: ProductUI)
}