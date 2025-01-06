package com.myattheingi.wexchanger.core.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "live_rates")
data class LiveRateEntity(
    @PrimaryKey val id: String = "live_rates", // Use a unique identifier for this entity
    val rates: Map<String, Double>,
    val timestamp: Long
)