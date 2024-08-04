package com.example.lolaecu.core.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs

object DateUtils {

    private var dayOfWeek: String = ""
    private var time = ""

    fun getDateInformation() {
        try {
            val dayOfWeekFormat = SimpleDateFormat("EEEE", Locale("es", "ES"))
            dayOfWeekFormat.timeZone = TimeZone.getTimeZone("GMT-5")

            val timeFormat = SimpleDateFormat("HH:mm:ss", Locale("es", "ES"))
            timeFormat.timeZone = TimeZone.getTimeZone("GMT-5")

            time = timeFormat.format(Date())

            dayOfWeek = when (dayOfWeekFormat.format(Date())) {
                "lunes" -> "Lunes"
                "martes" -> "Martes"
                "miércoles" -> "Miercoles"
                "jueves" -> "Jueves"
                "viernes" -> "Viernes"
                "sábado" -> "Sabado"
                "domingo" -> "Domingo"
                else -> ""
            }
        } catch (e: Exception) {
            Log.e("getDateInformationException", e.stackTraceToString())
        }
    }

    fun isTimeInRange(time1: String, time2: String, time3: String): Boolean {
        try {
            // Date format for parsing time in "HH:mm:ss" format
            val format = SimpleDateFormat("HH:mm:ss", Locale.US)

            // Parse times into Date objects
            val startTime = format.parse(time2)
            val endTime = format.parse(time3)
            val timeToCheck = format.parse(time1)

            // Check if the first time is between the second and third time
            return if (timeToCheck != null) {
                timeToCheck.after(startTime) && timeToCheck.before(endTime)
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("isTimeInRangeException", e.stackTraceToString())
            return false
        }
    }

    fun getTimeDifferenceInMinutes(fromTime: String, toTime: String): Int {
        val dateFormat =
            SimpleDateFormat("yyMMddHHmmss", Locale("es", "ES"))
        val date1: Date = dateFormat.parse(fromTime)!!
        val date2: Date = dateFormat.parse(toTime)!!

        val differenceInMilliseconds = abs(date2.time - date1.time)
        val differenceInMinutes = differenceInMilliseconds / (60 * 1000)
        return differenceInMinutes.toInt()
    }

    fun getDayOfWeek(): String {
        return this.dayOfWeek
    }

    fun getTime(): String {
        return this.time
    }
}