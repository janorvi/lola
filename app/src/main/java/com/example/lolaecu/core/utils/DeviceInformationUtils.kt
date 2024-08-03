package com.example.lolaecu.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import java.math.BigInteger

class DeviceInformationUtils @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    /**
     * Get device IMEI and save it on prefs
     * **/
    @SuppressLint("HardwareIds")
    fun getDeviceIdentifier() {
        try {
            val telephonyManager: TelephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            val deviceIdentifier: String = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                telephonyManager.deviceId
            } else {
                val androidIDHexa = Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
                val androidIDInt: String = BigInteger(androidIDHexa, 16).toString()
                Log.i("deviceID:", "androidIDInt: $androidIDInt")
                if (androidIDInt.length <= 15) {
                    androidIDInt
                } else {
                    androidIDInt.slice(0..14)
                }
            }
            //DeviceInformation.setDeviceId("868808038415581")
            DeviceInformation.setDeviceId(deviceIdentifier)
            /*prefs.saveStorage(
                KeyPreferences.VEHICLE_IMEI,
                deviceIdentifier
            )*/
            Log.i("deviceID:", "getDeviceIdentifier: $deviceIdentifier")
        } catch (e: Exception) {
            Log.e("getDeviceIMEIError", e.stackTraceToString())
        }
    }


    /** Determines if the device has Internet connection or not **/
    fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        //If android version is 10 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }

                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }

                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            //else android version is 9 or lower
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }


}