package com.rudiridho.catalogapps.data.remote

import com.rudiridho.catalogapps.data.model.ProductResponse
import com.rudiridho.catalogapps.data.service.ProductApiService

interface ProductRemoteDataSource {
    suspend fun getProducts(): List<ProductResponse>
}

class ProductRemoteDataSourceImpl(private val productApiService: ProductApiService) :
    ProductRemoteDataSource {
    override suspend fun getProducts(): List<ProductResponse> = productApiService.getProducts()
}