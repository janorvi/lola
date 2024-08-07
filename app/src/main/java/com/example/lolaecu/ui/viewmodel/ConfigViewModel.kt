package com.example.lolaecu.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lolaecu.core.utils.Configuration
import com.example.lolaecu.core.utils.Constants
import com.example.lolaecu.core.utils.DeviceInformation
import com.example.lolaecu.data.model.ConfigRequestModel
import com.example.lolaecu.data.model.ConfigResponseModel
import com.example.lolaecu.data.model.Fare
import com.example.lolaecu.data.model.NetworkResult
import com.example.lolaecu.data.repository.ConfigRepository
import com.example.lolaecu.domain.useCases.frames.GetConfigUseCase
import com.example.mdt.UserApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val configRepository: ConfigRepository,
    private val getConfigUseCase: GetConfigUseCase
) : ViewModel() {

    private var _configResponseLD: MutableLiveData<ConfigResponseModel> = MutableLiveData()
    val configResponseLD: LiveData<ConfigResponseModel> = _configResponseLD

    private var _paymentRate: MutableLiveData<String> = MutableLiveData("")
    val paymentRate: LiveData<String> = _paymentRate

    private var _routeName: MutableLiveData<String> = MutableLiveData("")
    val routeName: LiveData<String> = _routeName

    private var _paymentFare: MutableLiveData<Fare> = MutableLiveData()
    val paymentFare: LiveData<Fare> = _paymentFare

    fun initConfigFlow(configRequestBody: ConfigRequestModel) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                configRepository.getConfigFlow.collect {
                    if (it) {
                        try {
                            val configResponse: NetworkResult<ConfigResponseModel> =
                                getConfigUseCase.getConfig(configRequestBody)
                            when (configResponse) {
                                is NetworkResult.ApiSuccess -> {
                                    //*************************************************************
//                                configResponse.data.resend = false

                                    //testing
                                    //configResponse.data.mode = "A"

                                    //configResponse.data.assign.route.routeShortName = Constants.ROUTE_NAME

                                    //configResponse.data.assign.route.employeeFullName = "ACONCHA RAMOS SAIR"
                                    //configResponse.data.assign.route.employeeId = 4

                                    //////////////////////////////

                                    //Set the isTransaction offline variable for validations
                                    DeviceInformation.isTransactionsOffline =
                                        configResponse.data.resend

                                    //*************************************************************

                                    //Stores the latest configuration on a helper Object
                                    Configuration.setConfiguration(configResponse.data)

                                    //save MDT imei in prefs
                                    UserApplication.prefs.saveStorage(
                                        Constants.MDT_IMEI,
                                        configResponse.data.assign.route.imei
                                    )

                                    _configResponseLD.postValue(configResponse.data)
                                }

                                is NetworkResult.ApiError -> Log.e(
                                    "getConfigException",
                                    configResponse.message
                                )

                                is NetworkResult.ApiException -> Log.e(
                                    "getConfigException",
                                    configResponse.e.stackTraceToString()
                                )
                            }
                        } catch (e: Exception) {
                            Log.e("getConfigUseCase.getConfigException", e.stackTraceToString())
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("getConfigFlowException", e.stackTraceToString())
            }
        }
    }

    fun setPaymentRate(rate: String) {
        _paymentRate.value = rate.ifBlank {
            "0"
        }
    }

    fun setRouteName(routeName: String) {
        Log.i("routeName", "routeName: $routeName")
        _routeName.value = routeName
    }

    fun setPaymentFare(routeFare: Fare) {
        _paymentFare.value = routeFare
    }
}