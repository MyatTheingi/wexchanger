package com.myattheingi.wexchanger.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.myattheingi.wexchanger.core.data.local.models.LiveRateEntity

@Dao
interface LiveRatesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLiveRates(liveRates: LiveRateEntity)

    @Transaction
    @Query("DELETE FROM live_rates")
    fun deleteAllLiveRates()

    @Query("SELECT * FROM live_rates WHERE id = 'live_rates'")
    fun getLiveRates(): LiveRateEntity?
}