package com.example.lolaecu.core.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object RtpUtils {

    private val longRtpFormat =
        SimpleDateFormat("yMMddHHmmssSSSSSS", Locale("es", "ES"))


    private val shortRtpFormat =
        SimpleDateFormat("yyMMddHHmmss", Locale("es", "ES"))

    private val currentDateFormatYYMM =
        SimpleDateFormat("yyMM", Locale("es", "ES"))


    private val currentDateFormatYYMMDDHHMM =
        SimpleDateFormat("yyMMddHHmm", Locale("es", "ES"))


    fun getCurrentDateYYMM(): String {
        this.currentDateFormatYYMM.timeZone = TimeZone.getTimeZone("GMT-5")
        return this.currentDateFormatYYMM.format(Date())
    }

    fun getCurrentDateYYMMDDHHMM(): String {
        this.currentDateFormatYYMMDDHHMM.timeZone = TimeZone.getTimeZone("GMT-5")
        return this.currentDateFormatYYMMDDHHMM.format(Date())
    }

    fun getLongRtp(): String {
        this.longRtpFormat.timeZone = TimeZone.getTimeZone("GMT-5")
        return this.longRtpFormat.format(Date())
    }

    fun getShortRtp(): String {
        this.shortRtpFormat.timeZone = TimeZone.getTimeZone("GMT-5")
        return this.shortRtpFormat.format(Date())
    }

    fun convertShortToLongRTP(shortRtp: String): String {
        return try {
            "20" + shortRtp + "000000"
        } catch (e: Exception) {
            Log.e("convertShortToLongRTPException", e.stackTraceToString())
            ""
        }
    }

    fun convertLongToShortRTP(longRtp: String): String {
        return try {
            val longRTPLastIndex: Int = longRtp.lastIndex - 6
            longRtp.slice(2..longRTPLastIndex)

        } catch (e: Exception) {
            Log.e("convertLongToShortRTPException", e.stackTraceToString())
            ""
        }
    }
}