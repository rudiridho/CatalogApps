package com.rudiridho.catalogapps.domain.usecase

import com.rudiridho.catalogapps.data.model.ProductResponse
import com.rudiridho.catalogapps.domain.model.ProductDomain
import com.rudiridho.catalogapps.domain.repository.ProductRepository
import com.rudiridho.catalogapps.utils.DomainResult
import kotlinx.coroutines.flow.Flow

class GetProductsUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(): Flow<DomainResult<List<ProductDomain>>> =
        productRepository.getProducts()
}