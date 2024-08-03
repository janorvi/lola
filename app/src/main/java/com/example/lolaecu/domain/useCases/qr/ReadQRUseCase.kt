package com.example.lolaecu.domain.useCases.qr

import android.app.Activity
import android.util.Log
import com.example.lolaecu.core.utils.Constants
import com.example.lolaecu.data.repository.QRRepository
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import javax.inject.Inject

class ReadQRUseCase @Inject constructor(
    private val qrRepository: QRRepository,
) {

    operator fun invoke(
        barcodeView: DecoratedBarcodeView,
        activity: Activity,
        callback: BarcodeCallback,
        deviceModel: String
    ) {
        try {
            when (deviceModel) {
                /** Telpo TPS530**/
                Constants.TELPO_TPS530_DEVICE_MODEL -> qrRepository.telpoQRReading(
                    barcodeView = barcodeView,
                    activity = activity,
                    callback = callback
                )

                /** Telpo T20 **/
                Constants.TELPO_T20_DEVICE_MODEL -> qrRepository.telpoQRReading(
                    barcodeView = barcodeView,
                    activity = activity,
                    callback = callback
                )

                /** Telpo TPS320 **/
                Constants.TELPO_TPS320_DEVICE_MODEL -> qrRepository.telpoQRReading(
                    barcodeView = barcodeView,
                    activity = activity,
                    callback = callback
                )

                else -> qrRepository.cellPhoneQRReading(
                    barcodeView = barcodeView,
                    activity = activity,
                    callback = callback
                )
            }
        } catch (e: Exception) {
            Log.e("initQRReadingException", e.stackTraceToString())
        }
    }
}