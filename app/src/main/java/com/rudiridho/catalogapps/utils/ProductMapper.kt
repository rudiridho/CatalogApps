package com.rudiridho.catalogapps.utils

import com.rudiridho.catalogapps.data.model.ProductResponse
import com.rudiridho.catalogapps.domain.model.ProductDomain
import com.rudiridho.catalogapps.presentation.model.ProductUI

interface ProductMapper {
    fun mapResponseToDomain(response: List<ProductResponse?>): List<ProductDomain>
    suspend fun mapDomainToUI(domainModel: List<ProductDomain>): List<ProductUI>
}