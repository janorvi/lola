package com.example.lolaecu.ui.viewmodel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lolaecu.core.utils.PaymentTransaction
import com.example.lolaecu.core.utils.TransactionStatus
import com.example.lolaecu.data.model.MakeSaleResponseModel
import com.example.lolaecu.data.model.NetworkResult
import com.example.lolaecu.domain.model.MakeSaleRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.lolaecu.domain.useCases.qr.ReadQRUseCase
import com.example.lolaecu.domain.useCases.qr.QRPaymentUseCase
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import javax.inject.Inject

@HiltViewModel
class QRPaymentViewModel @Inject constructor(
    private val readQRUseCase: ReadQRUseCase,
    private val qrPaymentUseCase: QRPaymentUseCase,
) : ViewModel() {

    private var _qrString: MutableLiveData<String> = MutableLiveData()
    val qrString: LiveData<String> = _qrString

    private var _qrPaymentResponse: MutableLiveData<MakeSaleResponseModel> = MutableLiveData()
    val qrPaymentResponse: LiveData<MakeSaleResponseModel> = _qrPaymentResponse

    /** SetUp QR reader listener depending on Validator device model **/
    fun setUpQrReader(
        barcodeView: DecoratedBarcodeView,
        activity: Activity,
        deviceModel: String,
    ) {
        readQRUseCase(
            barcodeView = barcodeView,
            activity = activity,
            callback = callback,
            deviceModel = deviceModel
        )
    }

    /** Callback to QRCode reading for TPS530 validator **/
    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            try {
                // Prevent duplicate scans
                if (result.text == null || result.text == _qrString.value) {
                    return
                } else {
                    viewModelScope.launch(Dispatchers.IO) {
                        if (!TransactionStatus.isTransactionInProgress) {
                            _qrString.postValue(result.text)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("barcodeResultException", e.stackTraceToString())
            }
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {
        }
    }

    /** Process a QR payment and proceeds depending on api response **/
    fun makeQrPayment(makePaymentRequestBody: MakeSaleRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                /*when (val qrPaymentResponse =
                    qrPaymentUseCase.qrPaymentUseCase(makePaymentRequestBody.toModel())
                ) {
                    is NetworkResult.ApiSuccess -> {
                        PaymentTransaction.setPaymentTransaction(makePaymentRequestBody)
                        _qrPaymentResponse.postValue(qrPaymentResponse.data)
                    }

                    is NetworkResult.ApiError ->
                        Log.e("qrPaymentError", qrPaymentResponse.message)

                    is NetworkResult.ApiException -> throw (qrPaymentResponse.e)
                }*/
            } catch (e: Exception) {
                Log.e("makeQrPaymentException", e.stackTraceToString())
            }
        }
    }
}