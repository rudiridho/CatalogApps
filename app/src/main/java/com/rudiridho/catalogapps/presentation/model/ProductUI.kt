package com.rudiridho.catalogapps.presentation.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productui")
data class ProductUI(
    @PrimaryKey
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val isFavorite: Boolean = false
)