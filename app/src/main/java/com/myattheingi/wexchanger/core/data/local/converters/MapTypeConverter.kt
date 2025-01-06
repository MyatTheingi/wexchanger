package com.myattheingi.wexchanger.core.data.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MapTypeConverter {

    @TypeConverter
    fun fromStringMap(map: Map<String, String>?): String {
        return Gson().toJson(map)
    }

    @TypeConverter
    fun toStringMap(json: String): Map<String, String>? {
        val type = object : TypeToken<Map<String, String>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun fromDoubleMap(map: Map<String, Double>?): String {
        return Gson().toJson(map)
    }

    @TypeConverter
    fun toDoubleMap(json: String): Map<String, Double>? {
        val type = object : TypeToken<Map<String, Double>>() {}.type
        return Gson().fromJson(json, type)
    }
}
