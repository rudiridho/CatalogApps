package com.rudiridho.catalogapps.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rudiridho.catalogapps.presentation.model.ProductUI

@Database(entities = [ProductUI::class], version = 2)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}