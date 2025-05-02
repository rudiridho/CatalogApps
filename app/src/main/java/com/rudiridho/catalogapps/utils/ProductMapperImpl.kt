package com.rudiridho.catalogapps.utils

import com.rudiridho.catalogapps.data.model.ProductResponse
import com.rudiridho.catalogapps.domain.model.ProductDomain
import com.rudiridho.catalogapps.presentation.model.ProductUI
import kotlinx.coroutines.withContext
import java.util.UUID

class ProductMapperImpl(private val dispatcher: CoroutinesDispatcherProvider) : ProductMapper {
    override fun mapResponseToDomain(response: List<ProductResponse?>): List<ProductDomain> {
        return response.map { productResponse ->
            ProductDomain(
                id = productResponse?.tempId ?: UUID.randomUUID().toString(),
                name = productResponse?.name.orEmpty(),
                description = productResponse?.description.orEmpty(),
                price = productResponse?.price ?: 0.0,
                imageUrl = productResponse?.imageUrl.orEmpty(),
                isFavorite = productResponse?.isFavorite ?: false
            )}
    }

    override suspend fun mapDomainToUI(domainModel: List<ProductDomain>): List<ProductUI> =
        withContext(dispatcher.io) {
            domainModel.map { productDomain ->
                ProductUI(
                    id = productDomain.id,
                    name = productDomain.name,
                    description = productDomain.description,
                    price = productDomain.price,
                    imageUrl = productDomain.imageUrl,
                    isFavorite = productDomain.isFavorite
                )
            }
        }
}