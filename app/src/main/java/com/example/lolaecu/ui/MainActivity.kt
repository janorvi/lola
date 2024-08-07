package com.example.lolaecu.ui

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.example.lolaecu.core.utils.Constants
import com.example.lolaecu.core.utils.DeviceInformation
import com.example.lolaecu.core.utils.KeyPreferences
import com.example.lolaecu.core.utils.PermissionRequest
import com.example.lolaecu.data.model.ConfigRequestModel
import com.example.lolaecu.databinding.ActivityMainBinding
import com.example.lolaecu.ui.viewmodel.ConfigViewModel
import com.example.lolaecu.ui.viewmodel.FramesViewModel
import com.example.lolaecu.ui.viewmodel.UtilsViewModel
import com.example.mdt.UserApplication
import com.example.mdt.viewmodel.ApplicationViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val configViewModel: ConfigViewModel by viewModels()
    private val utilsViewModel: UtilsViewModel by viewModels()

    private var requestPermission: PermissionRequest = PermissionRequest()
    private val framesViewModel: FramesViewModel by viewModels()

    @Inject
    lateinit var applicationViewModel: ApplicationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        requestAppPermissions()
        utilsViewModel.getDeviceIdentifier()
        hideUIBars()
        initValidatorConfigFlow()
        initFlows()
        setContentView(binding.root)
    }

    private fun initFlows() {

        Log.i("deviceID:", "DeviceInformation.deviceID: ${DeviceInformation.getDeviceId()}")
        Log.i(
            "deviceID:", "Prefs: ${
                UserApplication.prefs.getStorage(
                    KeyPreferences.VEHICLE_IMEI
                )
            }"
        )

        //F20 flow
        framesViewModel.initF20FrameFlow()
    }

    private fun requestAppPermissions() {
        val permissionsToBeRequestedList: MutableList<String> = mutableListOf(
            Manifest.permission.NFC,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (Build.VERSION.SDK_INT >= 31) {
            val permissions = listOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
            )
            permissionsToBeRequestedList.addAll(permissions)
        }
        requestPermission.requestPermission(
            context = applicationContext,
            permissionsArray = permissionsToBeRequestedList.toTypedArray<String>(),
            activity = this
        )
    }

    private fun hideUIBars() {
        window.decorView.apply {
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }

    private fun initValidatorConfigFlow() {
        configViewModel.initConfigFlow(
            ConfigRequestModel(
                imei = DeviceInformation.getDeviceId().ifBlank {
                    UserApplication.prefs.getStorage(
                        Constants.MDT_IMEI
                    )
                }
            )
        )
    }
}