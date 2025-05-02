package com.rudiridho.catalogapps.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudiridho.catalogapps.data.model.ProductResponse
import com.rudiridho.catalogapps.domain.model.ProductDomain
import com.rudiridho.catalogapps.domain.usecase.GetFavoriteProductsUseCase
import com.rudiridho.catalogapps.domain.usecase.GetProductsUseCase
import com.rudiridho.catalogapps.domain.usecase.SearchProductsUseCase
import com.rudiridho.catalogapps.domain.usecase.UpdateProductUseCase
import com.rudiridho.catalogapps.presentation.model.ProductUI
import com.rudiridho.catalogapps.utils.DomainResult
import com.rudiridho.catalogapps.utils.ProductMapper
import com.rudiridho.catalogapps.utils.UiSafeState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProductViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val getFavoriteProductsUseCase: GetFavoriteProductsUseCase,
    private val searchProductsUseCase: SearchProductsUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val productMapper: ProductMapper
) : ViewModel() {

    private val _products = MutableLiveData<UiSafeState<List<ProductUI>>>(UiSafeState.Uninitialized)
    val products: LiveData<UiSafeState<List<ProductUI>>> = _products

    private val _favoriteProducts = MutableLiveData<UiSafeState<List<ProductUI>>>()
    val favoriteProducts: LiveData<UiSafeState<List<ProductUI>>> = _favoriteProducts

    private val _searchedProducts = MutableLiveData<UiSafeState<List<ProductUI>>>()
    val searchedProducts: LiveData<UiSafeState<List<ProductUI>>> = _searchedProducts

    init {
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            getProductsUseCase().onEach { result ->
                _products.value = when (result) {
                    is DomainResult.Success -> UiSafeState.Success(
                        productMapper.mapDomainToUI(
                            result.data
                        )
                    )
                    is DomainResult.EmptyState -> UiSafeState.Empty
                    is DomainResult.ErrorState -> UiSafeState.Error(result.message.toString())
                    DomainResult.NetworkError -> UiSafeState.ErrorConnection
                    is DomainResult.TechnicalError -> UiSafeState.Error(
                        result.message.toString(),
                        result.code
                    )
                }
            }.launchIn(viewModelScope)
        }
    }

    fun getFavoriteProducts() {
        viewModelScope.launch {
            getFavoriteProductsUseCase().onEach { result ->
                _favoriteProducts.value = when (result) {
                    is DomainResult.Success -> UiSafeState.Success(productMapper.mapDomainToUI(result.data))
                    is DomainResult.EmptyState -> UiSafeState.Empty
                    is DomainResult.ErrorState -> UiSafeState.Error(result.message.toString())
                    DomainResult.NetworkError -> UiSafeState.ErrorConnection
                    is DomainResult.TechnicalError -> UiSafeState.Error(
                        result.message.toString(),
                        result.code
                    )
                }
            }.launchIn(viewModelScope)
        }
    }

    fun searchProducts(query: String) {
        viewModelScope.launch {
            searchProductsUseCase(query).onEach { result ->
                _searchedProducts.value = when (result) {
                    is DomainResult.Success -> UiSafeState.Success(productMapper.mapDomainToUI(result.data))
                    is DomainResult.EmptyState -> UiSafeState.Empty
                    is DomainResult.ErrorState -> UiSafeState.Error(result.message.toString())
                    DomainResult.NetworkError -> UiSafeState.ErrorConnection
                    is DomainResult.TechnicalError -> UiSafeState.Error(
                        result.message.toString(),
                        result.code
                    )}
            }.launchIn(viewModelScope)
        }
    }

    fun updateProduct(product: ProductUI) {
        viewModelScope.launch {
            updateProductUseCase(product)
        }
    }
}