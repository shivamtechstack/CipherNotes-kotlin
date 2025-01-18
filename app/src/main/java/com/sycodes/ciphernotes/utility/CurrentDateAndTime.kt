package com.sycodes.ciphernotes.utility

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CurrentDateAndTime {
    fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        return dateFormat.format(Date())
    }
}