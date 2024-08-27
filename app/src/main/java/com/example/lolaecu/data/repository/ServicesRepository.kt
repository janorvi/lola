package com.example.lolaecu.data.repository

import android.util.Log
import com.example.lolaecu.core.utils.Constants
import com.example.lolaecu.core.utils.KeyPreferences
import com.example.lolaecu.data.model.DataServiceModel
import com.example.mdt.UserApplication
import com.example.mdt.model.ServiceModel
import com.example.mdt.model.ServicePost
import com.example.mdt.viewmodel.ApplicationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ServicesRepository @Inject constructor(
    private val applicationViewModel: ApplicationViewModel
) {

    /** get services flow **/
    val getServicesFlow: Flow<Boolean> = flow {
        while(true) {
            delay(10000L)
            emit(true)
        }
    }.flowOn(Dispatchers.IO)

    fun getServicesFromSAE(getServicesRequestBody: ServicePost) {
        applicationViewModel.getServicesSAE(getServicesRequestBody)
    }

    fun updateGUIRouteIdById(routeID: Int) {
        applicationViewModel.updateGUIRouteIdbyId(
            id = Constants.idSave,
            guiRouteId = routeID
        )
    }
}