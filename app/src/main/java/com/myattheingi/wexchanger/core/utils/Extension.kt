package com.myattheingi.wexchanger.core.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.text.ParseException
import java.util.Locale

fun String?.toNullableDouble(locale: Locale = Locale.getDefault()): Double? {
    if (this.isNullOrBlank()) return null

    return try {
        val numberFormatter = NumberFormat.getNumberInstance(locale)
        numberFormatter.parse(this)?.toDouble()
    } catch (e: ParseException) {
        null // Return null if the string cannot be parsed
    }
}


fun Double?.toFormattedString(locale: Locale = Locale.getDefault()): String {
    if (this == null) return ""

    // Convert to BigDecimal to avoid scientific notation
    val bigDecimalValue = BigDecimal(this).stripTrailingZeros()

    // Determine the scale based on the value
    val scale = if (this >= 1) 2 else 9
    val scaledValue = bigDecimalValue.setScale(scale, RoundingMode.HALF_UP)

    // Format the number using NumberFormat
    val numberFormatter = NumberFormat.getNumberInstance(locale)
    numberFormatter.maximumFractionDigits = scale
    numberFormatter.minimumFractionDigits = if (this >= 1) 2 else 0

    // Return the formatted number
    return numberFormatter.format(scaledValue)
}

fun Double?.toLocaleString(): String {
    return this?.toString() ?: ""
}

