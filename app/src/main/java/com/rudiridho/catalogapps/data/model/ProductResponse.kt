package com.rudiridho.catalogapps.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("price")
    val price: Double? = null,
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
    var isFavorite: Boolean = false,
    val tempId: String? = null
)
