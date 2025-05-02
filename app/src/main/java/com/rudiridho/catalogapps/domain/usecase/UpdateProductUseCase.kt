package com.rudiridho.catalogapps.domain.usecase

import com.rudiridho.catalogapps.domain.repository.ProductRepository
import com.rudiridho.catalogapps.presentation.model.ProductUI

class UpdateProductUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(product: ProductUI) = productRepository.updateProduct(product)
}