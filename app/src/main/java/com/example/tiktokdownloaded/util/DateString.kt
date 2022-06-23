package com.example.tiktokdownloaded.util

import java.text.SimpleDateFormat
import java.util.*

class DateString {
    companion object{

        val date = getCurrentDateTime()
        val dateInString = date.toString("dd/MM/yyyy")

        fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
            val formatter = SimpleDateFormat(format, locale)
            return formatter.format(this)
        }

        fun getCurrentDateTime(): Date {
            return Calendar.getInstance().time
        }
    }
}