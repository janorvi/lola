package com.example.lolaecu.core.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import javax.inject.Inject

class PermissionRequest @Inject constructor(
) {

    fun requestPermission(
        context: Context,
        permissionsArray: Array<String>,
        activity: Activity
    ) {

//        var permissionCheck = ContextCompat.checkSelfPermission(
//            context,
//            permissionsArray.last()
//        )
//
//        while (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(
//                activity,
//                permissionsArray,
//                0
//            )
//            permissionCheck = ContextCompat.checkSelfPermission(
//                context,
//                permissionsArray.last()
//            )
//        }

        try {
            permissionsArray.forEach {
                while (ContextCompat.checkSelfPermission(
                        context,
                        it
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
                    ActivityCompat.requestPermissions(activity, arrayOf(it), 0)
                }
            }
        } catch (e: Exception) {
            Log.e("requestPermissionException", e.stackTraceToString())
        }

    }
}