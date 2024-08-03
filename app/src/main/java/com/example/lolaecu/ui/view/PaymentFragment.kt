package com.example.lolaecu.ui.view

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.lolaecu.core.utils.DeviceInformation
import com.example.lolaecu.core.utils.PermissionRequest
import com.example.lolaecu.core.utils.TransactionStatus
import com.example.lolaecu.databinding.FragmentPaymentBinding
import com.example.lolaecu.ui.viewmodel.QRPaymentViewModel
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : Fragment() {

    private lateinit var barcodeView: DecoratedBarcodeView
    private val qrPaymentViewModel: QRPaymentViewModel by viewModels()

    private var imei = ""
    private var isPaymentMethodBeingRead = false

    //Device info
    private lateinit var deviceModel: String

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTransactionVariables()

        deviceModel = DeviceInformation.deviceModel
        //Init observers and methods
        //setUpUI()
        //initListeners()
        initQRScanner()
        //initLolaObservers()
        //initLibraryObservers()
        //initPorts()
        //isPaymentMethodBeingReadListener()
        //initFlows()
    }

    private fun initTransactionVariables() {
        TransactionStatus.isTransactionInProgress = false
        isPaymentMethodBeingRead = false
        DeviceInformation.isCardBeingRead = false
    }

    private fun initQRScanner() {
        try {
            PermissionRequest().requestPermission(
                requireContext(),
                arrayOf(
                    Manifest.permission.CAMERA,
                ),
                requireActivity()
            )

            barcodeView = binding.zxingBarcodeScanner

            qrPaymentViewModel.setUpQrReader(
                barcodeView = barcodeView,
                activity = requireActivity(),
                deviceModel = deviceModel
            )
        } catch (e: Exception) {
            Log.e("initQRScannerException", e.stackTraceToString())
        }
    }

    override fun onResume() {
        super.onResume()
        barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()
    }
}