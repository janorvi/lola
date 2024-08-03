package com.example.lolaecu.ui.viewmodel

import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lolaecu.core.utils.DeviceInformation
import com.example.lolaecu.core.utils.DeviceInformationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UtilsViewModel @Inject constructor(
    private val deviceInformationUtils: DeviceInformationUtils,
) : ViewModel() {


    private var _isPaymentMethodBeingRead: MutableLiveData<Boolean> = MutableLiveData(false)
    val isPaymentMethodBeingRead: LiveData<Boolean> = _isPaymentMethodBeingRead

    fun getDeviceIdentifier() {
        try {
            deviceInformationUtils.getDeviceIdentifier()
            DeviceInformation.deviceModel = Build.MODEL
        } catch (e: Exception) {
            Log.e("getDeviceIdentifierException", e.stackTraceToString())
        }
    }

    fun readingPaymentMethodStarted() {
//        viewModelScope.launch(Dispatchers.IO) {
            _isPaymentMethodBeingRead.postValue(true)
//        }
    }

    fun readingPaymentMethodFinished() {
//        viewModelScope.launch(Dispatchers.IO) {
            _isPaymentMethodBeingRead.postValue(false)
//        }
    }
}