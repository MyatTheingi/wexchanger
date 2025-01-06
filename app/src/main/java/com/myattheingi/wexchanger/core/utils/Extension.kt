package com.myattheingi.wexchanger.core.utils

import android.util.Log
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

fun String?.asNumber(locale: Locale = Locale.getDefault()): Number {
    if (isNullOrBlank()) return 0

    val numberFormatter = NumberFormat.getNumberInstance(locale)
    return numberFormatter.parse(this.toString()) ?: 0
}

// Extension function for Double to round to at most 9 decimal places
fun Double.roundTo9DecimalPlaces(): String {
    return BigDecimal(this)
        .setScale(9, RoundingMode.HALF_UP)
        .stripTrailingZeros() // Removes unnecessary trailing zeros
        .toPlainString() // Ensures no scientific notation
}