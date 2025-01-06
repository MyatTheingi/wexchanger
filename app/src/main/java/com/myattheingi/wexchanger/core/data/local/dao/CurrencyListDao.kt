package com.myattheingi.wexchanger.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myattheingi.wexchanger.core.data.local.models.CurrencyListEntity

@Dao
interface CurrencyListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrencyList(currencyList: CurrencyListEntity)

    @Query("SELECT * FROM currency_list WHERE id = 'currency_list'")
    fun getCurrencyList(): CurrencyListEntity?
}