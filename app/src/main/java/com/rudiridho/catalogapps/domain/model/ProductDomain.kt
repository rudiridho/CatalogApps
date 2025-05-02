package com.rudiridho.catalogapps.domain.model

data class ProductDomain(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val isFavorite: Boolean = false
)