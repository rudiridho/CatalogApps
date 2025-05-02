package com.rudiridho.catalogapps.data.repository

import com.rudiridho.catalogapps.data.local.ProductLocalDataSource
import com.rudiridho.catalogapps.data.remote.ProductRemoteDataSource
import com.rudiridho.catalogapps.domain.model.ProductDomain
import com.rudiridho.catalogapps.domain.repository.ProductRepository
import com.rudiridho.catalogapps.presentation.model.ProductUI
import com.rudiridho.catalogapps.utils.Constants
import com.rudiridho.catalogapps.utils.DomainResult
import com.rudiridho.catalogapps.utils.ProductMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

class ProductRepositoryImpl(
    private val remoteDataSource: ProductRemoteDataSource,
    private val localDataSource: ProductLocalDataSource,
    private val productMapper: ProductMapper
) : ProductRepository {
    override suspend fun getProducts(): Flow<DomainResult<List<ProductDomain>>> = flow {
        try {
            val remoteProducts = remoteDataSource.getProducts()
            if (remoteProducts.isEmpty()) {
                emit(DomainResult.EmptyState("No products found"))
            } else {
                val productDomain = productMapper.mapResponseToDomain(remoteProducts)
                val productUI = productMapper.mapDomainToUI(productDomain)
                localDataSource.insertProducts(productUI)
                emit(DomainResult.Success(productDomain))
            }
        } catch (e: HttpException) {
            emit(DomainResult.TechnicalError(Constants.RESPONSE_ERROR_CONST, e.message))
        } catch (e: IOException) {
            emit(DomainResult.NetworkError)
        } catch (e: Exception) {
            emit(DomainResult.TechnicalError(Constants.SSL_ERROR_CONST, e.message))
        }
    }

    override suspend fun getFavoriteProducts(): Flow<DomainResult<List<ProductDomain>>> =
        localDataSource.getFavoriteProducts().map { productUIList ->
            val productDomainList = productUIList.map {
                ProductDomain(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    price = it.price,
                    imageUrl = it.imageUrl,
                    isFavorite = it.isFavorite
                )
            }
            DomainResult.Success(productDomainList)
        }

    override suspend fun searchProducts(query: String): Flow<DomainResult<List<ProductDomain>>> =
        localDataSource.searchProducts(query).map { productUIList ->
            val productDomainList = productUIList.map {
                ProductDomain(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    price = it.price,
                    imageUrl = it.imageUrl,
                    isFavorite = it.isFavorite
                )
            }
            DomainResult.Success(productDomainList)
        }

    override suspend fun updateProduct(product: ProductUI) = localDataSource.updateProduct(product)
}