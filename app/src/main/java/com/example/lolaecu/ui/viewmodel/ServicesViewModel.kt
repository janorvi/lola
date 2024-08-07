package com.example.lolaecu.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lolaecu.core.utils.Configuration
import com.example.lolaecu.core.utils.Constants
import com.example.lolaecu.data.model.ConfigResponseModel
import com.example.lolaecu.data.repository.ServicesRepository
import com.example.lolaecu.domain.useCases.services.GetServicesUseCase
import com.example.mdt.UserApplication
import com.example.mdt.model.ServiceModel
import com.example.mdt.model.ServicePost
import com.example.mdt.viewmodel.ApplicationViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServicesViewModel @Inject constructor(
    private val getServicesUseCase: GetServicesUseCase,
    private val servicesRepository: ServicesRepository
) : ViewModel() {

    //Support variables
    var isServiceRunning = false

    fun initGetServicesFlow() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                servicesRepository.getServicesFlow.catch {
                    Log.e("getServicesFlowException", it.stackTraceToString())
                }
                    .collect {
                        if (it && isServiceRunning.not()) {
                            try {
                                val servicesRequestBody = buildGetServiceRequestBody()
                                Log.i(
                                    "servicesRequestBody",
                                    "servicesRequestBody: $servicesRequestBody"
                                )
                                getServicesUseCase(servicesRequestBody)
                            } catch (e: Exception) {
                                Log.e("initGetServicesFlowException", e.stackTraceToString())
                            }
                        }
                    }
            } catch (e: Exception) {
                Log.e("initGetServicesFlowException", e.stackTraceToString())
            }
        }
    }

    private fun buildGetServiceRequestBody(): ServicePost {
        return try {
            val validatorConfig: ConfigResponseModel = Configuration.getConfiguration()
            ServicePost(
                employee_id = validatorConfig.assign.route.employeeId,
                pso_routes = arrayListOf(),
                detail_service_id = null,
                status = 1,
                type = "selina",
                imei = validatorConfig.assign.route.imei
            )
        } catch (e: Exception) {
            Log.e("buildGetServiceRequestBodyException", e.stackTraceToString())
            ServicePost(
                employee_id = null,
                pso_routes = null,
                detail_service_id = null,
                status = null,
                type = null,
                imei = null
            )
        }
    }

    fun updateGUIRouteIdById(routeID: Int){
        servicesRepository.updateGUIRouteIdById(routeID)
    }
}