package com.example.lolaecu.ui.viewmodel

import android.os.Environment
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
import com.example.mdt.viewmodel.ApplicationViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val configRepository: ConfigRepository,
    private val getConfigUseCase: GetConfigUseCase,
    private val applicationViewModel: ApplicationViewModel
) : ViewModel() {

    private var _configResponseLD: MutableLiveData<ConfigResponseModel> = MutableLiveData()
    val configResponseLD: LiveData<ConfigResponseModel> = _configResponseLD

    private var _paymentRate: MutableLiveData<String> = MutableLiveData("")
    val paymentRate: LiveData<String> = _paymentRate

    private var _routeName: MutableLiveData<String> = MutableLiveData("")
    val routeName: LiveData<String> = _routeName

    private var _paymentFare: MutableLiveData<Fare> = MutableLiveData()
    val paymentFare: LiveData<Fare> = _paymentFare

    var previousHardwareConfiguration = ""
    var currentHardwareConfiguration = ""

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

                                    currentHardwareConfiguration = Gson().toJson(configResponse.data.hardwareConfiguration)

                                    if(previousHardwareConfiguration != currentHardwareConfiguration){
                                        Log.i("updateConfiguration", "Update configuration")
                                        applicationViewModel.createHubTransaction("HUB", "setHardwareConfig", currentHardwareConfiguration)
                                        previousHardwareConfiguration = currentHardwareConfiguration
                                    }
                                    //saveHardwareConfiguration(Gson().toJson(configResponse.data.hardwareConfiguration))

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

    fun saveHardwareConfiguration(configuration: String) {
        val c = Calendar.getInstance()

        //val texto = "[$anoActual-$mesActual-$diaMesActual $horaActual:$minutoActual:$segundoActual] - [$tipoMensajeAGuardar] - Mensaje: $mensajeAGuardar"

        val texto = configuration

        val externalStorageDir = File(Environment.getExternalStorageDirectory().absolutePath)
        val fileName: String = "configuration.txt"

        // crear la carpate si no existe
        val statement = externalStorageDir.exists() && externalStorageDir.isDirectory
        if (!statement) {
            // do something here
            externalStorageDir.mkdirs()
        }

        // crear el archivo de texto si no existe
        val myFile = File(externalStorageDir.absolutePath, fileName)
        if (!myFile.exists()) {
            try {
                myFile.createNewFile()
            } catch (e: Exception) {
                println("Error al crear el archivo: ${e.message}")
            }
        } else {
            // si el archivo existe verificar el tamaño que tiene que no vaya a superar 1MB
            val tamanoArchivo = myFile.length() / 1000
            val tamanoMaximo = 10485
            if (tamanoArchivo >= tamanoMaximo) {
                //renombrar archivo
                val pathArchivoAntiguo = myFile.absolutePath
                val pathNuevo = pathArchivoAntiguo.substring(0, pathArchivoAntiguo.lastIndexOf(".txt"))
                var numeroArchivoNuevo = 1
                var archivoNuevo = File("$pathNuevo$numeroArchivoNuevo.txt")
                while (archivoNuevo.exists()) {
                    //si el archivo nuevo no existe renombrar el archivo actual por uno nuevo
                    numeroArchivoNuevo += 1
                    archivoNuevo = File("$pathNuevo$numeroArchivoNuevo.txt")
                }
                val renombrarExitoso = myFile.renameTo(archivoNuevo)
                if (renombrarExitoso) {
                    // se renombro satisfactoriamente el archivo
                    println("Archivo $pathArchivoAntiguo renombrado con exito")
                } else {
                    // hubo un error al renombrar el archivo
                    println("Error al renombrar el archivo $pathArchivoAntiguo en el metodo savelog de la clase MainActivity")
                }
            }
        }

        // escribir en el archivo de texto
        try {
            //para escribir al final del archivo
            val fileWritter = FileWriter(myFile, false)
            val bufferWritter = BufferedWriter(fileWritter)
            bufferWritter.write(texto)
            bufferWritter.close()
        } catch (e: Exception) {
            println("Error al escribir en el archivo: ${e.message}")
        }
        //------------------------------------------------------------------------------------------
    }
}