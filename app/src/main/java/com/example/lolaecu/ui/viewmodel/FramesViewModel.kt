package com.example.lolaecu.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lolaecu.core.utils.Constants
import com.example.lolaecu.core.utils.KeyTransactions
import com.example.lolaecu.data.repository.FramesRepository
import com.example.lolaecu.domain.useCases.frames.F20FramesUseCase
import com.example.mdt.UserApplication
import com.example.mdt.viewmodel.ApplicationViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FramesViewModel @Inject constructor(
    private val f20FramesUseCase: F20FramesUseCase,
    private val framesRepository: FramesRepository,
    private val applicationViewModel: ApplicationViewModel
) : ViewModel() {

    fun initF20FrameFlow() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                applicationViewModel.runSendTransactionsCycle(5000L)
                framesRepository.frameF20Flow
                    .catch { Log.e("f20FrameFlowException", it.stackTraceToString()) }
                    .collect {
                        try {
                            f20FramesUseCase(KeyTransactions.CODIGO_REPORTE_FRECUENCIA)

                            //testing////////////////////////////////////////////////////////////////////////////////////
                            Log.i(
                                "servicePrefs",
                                "cycle: ${UserApplication.prefs.getStorage(Constants.ASSIGNED_SERVICE)}"
                            )
                            ////////////////////////////////////////////////////////////////////////////////////////////

                        } catch (e: Exception) {
                            Log.e("f20FrameFlowCollectException", e.stackTraceToString())
                        }
                    }
            } catch (e: Exception) {
                Log.e("sendF20FrameException", e.stackTraceToString())
            }
        }
    }
}