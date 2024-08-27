package com.example.lolaecu.data.repository

import com.example.lolaecu.core.utils.Configuration
import com.example.lolaecu.core.utils.TransactionStatus
import com.example.mdt.UserApplication
import com.example.mdt.model.GetTransactionFrameBodyModel
import com.example.mdt.viewmodel.ApplicationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FramesRepository @Inject constructor(
    private val applicationViewModel: ApplicationViewModel
) {
    /** F20 frame Flow **/
    val frameF20Flow: Flow<Int> = flow {
        while (true) {
            emit(1)
            delay(10000L)
        }
    }
        .flowOn(Dispatchers.IO)

    suspend fun sendSelinaFrame(frameType: String) {
        val configuration = Configuration.getConfiguration()

        val frameBody = GetTransactionFrameBodyModel(
            mode = configuration.mode,
            framesCount = TransactionStatus.noSyncronizedFrames,
            frameType = frameType,
            //driverId = configuration.assign.route.employeeId.toString(),
            driverId = UserApplication.prefs.getStorage(
                "id"
            ),
            vehicle = configuration.assign.route.imei,
            sale = null,
            close = null,
            cameras = listOf()
        )
        applicationViewModel.getTransactionFrame(frameBody)
    }
}