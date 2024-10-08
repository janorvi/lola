package com.example.lolaecu.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.example.lolaecu.core.utils.Constants
import com.example.lolaecu.core.utils.DeviceInformation
import com.example.lolaecu.core.utils.KeyPreferences
import com.example.lolaecu.core.utils.KeyTransactions
import com.example.lolaecu.core.utils.PermissionRequest
import com.example.lolaecu.core.utils.TransactionStatus
import com.example.lolaecu.data.model.ConfigRequestModel
import com.example.lolaecu.databinding.ActivityMainBinding
import com.example.lolaecu.ui.viewmodel.ConfigViewModel
import com.example.lolaecu.ui.viewmodel.FramesViewModel
import com.example.lolaecu.ui.viewmodel.ServicesViewModel
import com.example.lolaecu.ui.viewmodel.UtilsViewModel
import com.example.mdt.UserApplication
import com.example.mdt.model.Services
import com.example.mdt.viewmodel.ApplicationViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val configViewModel: ConfigViewModel by viewModels()
    private val utilsViewModel: UtilsViewModel by viewModels()
    private val framesViewModel: FramesViewModel by viewModels()
    private val servicesViewModel: ServicesViewModel by viewModels()

    private var requestPermission: PermissionRequest = PermissionRequest()

    @Inject
    lateinit var applicationViewModel: ApplicationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        requestAppPermissions()
        utilsViewModel.getDeviceIdentifier()
        hideUIBars()
        initConfig()
        initFlows()
        selinaObservers()
        applicationViewModel.sendPendingTransactions("",9000)
        getDeviceCoordinates()
        setContentView(binding.root)
    }

    private fun initConfig() {
        try {
            //testing
            UserApplication.prefs.saveStorage(
                name = Constants.LOCATION_MODE,
                value = "0"
            )
            /////////////////////////////////////////////

            //init getting validator config
            initValidatorConfigFlow()

            //Get the latest simulation row index just in case the app was closed during a service
            val simulationRowIndex: String =
                UserApplication.prefs.getStorage(Constants.SIMULATION_ROUTE_INDEX)

            UserApplication.prefs.saveToken(Constants.API_TOKEN)

            UserApplication.prefs.saveStorage(
                name = Constants.routeId,
                value = "0"
            )

        } catch (e: Exception) {
            Log.e("initConfigException", e.stackTraceToString())
        }
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

        //Init get services flow
        servicesViewModel.initGetServicesFlow()

        //F20 flow
        framesViewModel.initF20FrameFlow()

        //Update pending transactions flow
        framesViewModel.updatePendingTransactionsFlow()
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
    private fun selinaObservers() {
        try {
            //////////////////////////////////////////////////////////////////////////////

            applicationViewModel.servicesSAEResponse.observe(this) { service ->
                //servicesViewModel.updateGUIRouteIdById(routeID = service.data.psoRoutes.first().idRoute)
                service.data.psoRoutes.forEach{ psoRoute ->
                    psoRoute.services.forEach { services ->
                        services.detailServices.forEach { detailServices ->
                            val s = detailServices.servicePsoRouteId
                            servicesViewModel.updateGUIRouteIdById(routeID = detailServices.servicePsoRouteId)
                            UserApplication.prefs.saveStorage(
                                Constants.driverId, detailServices.employeeId.toString()
                            )
                        }
                    }
                }
                Log.i("servicesSAEResponse", "$service")
            }

            applicationViewModel.serviceError.observe(this) { error ->
                Log.i("servicesSAEResponse", "servicesSAEResponse Error: ${error.second}")
            }

            applicationViewModel.configuration.observe(this) { config ->
                applicationViewModel.runSendTransactionsCycle(1500L)
            }

        } catch (e: Exception) {
            Log.e("selinaObserversException", e.stackTraceToString())
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

    private fun getDeviceCoordinates() {
        try {
            val locationManager: LocationManager = application
                .getSystemService(Context.LOCATION_SERVICE) as LocationManager

            val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            val gpsLocationListener = LocationListener { location ->
                UserApplication.prefs.saveStorage(KeyPreferences.LATITUD, location.latitude.toString())
                UserApplication.prefs.saveStorage(KeyPreferences.LONGITUD, location.longitude.toString())
            }

            val networkLocationListener = LocationListener { location ->
            }

            if (hasGps || hasNetwork) {

//                if (hasNetwork) {
//                    locationManager.requestLocationUpdates(
//                        /* provider = */ LocationManager.NETWORK_PROVIDER,
//                        /* minTimeMs = */ 5000,
//                        /* minDistanceM = */ 0F,
//                        /* listener = */ networkLocationListener
//                    )
//                }

                if (hasGps) {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return
                    }
                    locationManager.requestLocationUpdates(
                        /* provider = */ LocationManager.GPS_PROVIDER,
                        /* minTimeMs = */ 5000,
                        /* minDistanceM = */ 0F,
                        /* listener = */ gpsLocationListener
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("getDeviceCoordinatesException", e.stackTraceToString())
        }
    }

    override fun onDestroy() {
        servicesViewModel.updateGUIRouteIdById(routeID = 0)
        super.onDestroy()
    }
}