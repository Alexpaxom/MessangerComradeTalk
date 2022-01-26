package com.alexpaxom.homework_2.helpers

import java.text.SimpleDateFormat
import java.util.*

class DateFormatter {
    companion object {
        const val DATA_DELIMITER_FORMAT = "E, dd MMM"

        fun formatDate(date: Date, format: String): String {
            val sf = SimpleDateFormat(format, Locale.ROOT)
            return sf.format(date)
        }
    }
}