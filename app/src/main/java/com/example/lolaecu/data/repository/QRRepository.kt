package com.example.lolaecu.data.repository

import android.app.Activity
import android.util.Log
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import javax.inject.Inject

class QRRepository @Inject constructor(

) {
    fun telpoQRReading(
        barcodeView: DecoratedBarcodeView,
        activity: Activity,
        callback: BarcodeCallback
    ) {
        zxingQRReading(
            barcodeView = barcodeView,
            activity = activity,
            callback = callback
        )
    }
    fun cellPhoneQRReading(
        barcodeView: DecoratedBarcodeView,
        activity: Activity,
        callback: BarcodeCallback
    ) {
        zxingQRReading(
            barcodeView = barcodeView,
            activity = activity,
            callback = callback
        )
    }

    private fun zxingQRReading(
        barcodeView: DecoratedBarcodeView,
        activity: Activity?,
        callback: BarcodeCallback
    ) {
        try {
            val formats: ArrayList<BarcodeFormat> =
                arrayListOf(BarcodeFormat.QR_CODE, BarcodeFormat.AZTEC, BarcodeFormat.CODABAR)
            barcodeView.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
            barcodeView.initializeFromIntent(activity?.intent)
            barcodeView.decodeContinuous(callback)
//        val beepManager = BeepManager(activity)
        } catch (e: Exception) {
            Log.e("zxingQRReadingException", e.stackTraceToString())
        }
    }


}