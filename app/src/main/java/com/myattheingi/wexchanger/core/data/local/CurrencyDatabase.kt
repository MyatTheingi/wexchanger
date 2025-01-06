package com.myattheingi.wexchanger.core.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.myattheingi.wexchanger.core.data.local.converters.MapTypeConverter
import com.myattheingi.wexchanger.core.data.local.dao.CurrencyListDao
import com.myattheingi.wexchanger.core.data.local.dao.LiveRatesDao
import com.myattheingi.wexchanger.core.data.local.models.CurrencyListEntity
import com.myattheingi.wexchanger.core.data.local.models.LiveRateEntity

@Database(entities = [LiveRateEntity::class, CurrencyListEntity::class], version = 1)
@TypeConverters(
    MapTypeConverter::class,
)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun liveRatesDao(): LiveRatesDao
    abstract fun currencyListDao(): CurrencyListDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: CurrencyDatabase? = null

        fun getDatabase(context: Context): CurrencyDatabase {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CurrencyDatabase::class.java,
                    "currency_database.db"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}