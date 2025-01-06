package com.myattheingi.wexchanger.core.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_list")
data class CurrencyListEntity(
    @PrimaryKey val id: String = "currency_list", // Unique identifier
    val currencies: Map<String, String>
)