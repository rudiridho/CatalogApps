package com.rudiridho.catalogapps.data.service

import com.rudiridho.catalogapps.data.model.ProductResponse
import retrofit2.http.GET

interface ProductApiService {
    @GET("/products")
    suspend fun getProducts(): List<ProductResponse>
}