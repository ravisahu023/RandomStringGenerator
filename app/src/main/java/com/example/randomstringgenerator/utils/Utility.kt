package com.example.randomstringgenerator.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object Utility {

    fun formatIsoDate(input: String): String {
        return try {
            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())

            val date = isoFormat.parse(input)

            val outputFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
            outputFormat.format(date!!)
        } catch (e: ParseException) {
            input // fallback to original if parsing fails
        }
    }

    fun isValidLength(input: String): Boolean {
        return input.toIntOrNull()?.let { it >= 0 } == true
    }

    fun getValidationError(input: String): String? {
        return if (!isValidLength(input)) "Please enter a valid positive number" else null
    }
}