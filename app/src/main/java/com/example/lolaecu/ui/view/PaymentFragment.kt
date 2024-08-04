package com.example.lolaecu.ui.view

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.lolaecu.core.utils.Configuration
import com.example.lolaecu.core.utils.Constants
import com.example.lolaecu.core.utils.DateUtils
import com.example.lolaecu.core.utils.DeviceInformation
import com.example.lolaecu.core.utils.PermissionRequest
import com.example.lolaecu.core.utils.TransactionStatus
import com.example.lolaecu.data.model.ConfigResponseModel
import com.example.lolaecu.data.model.Fares
import com.example.lolaecu.databinding.FragmentPaymentBinding
import com.example.lolaecu.domain.model.MakeSaleRequest
import com.example.lolaecu.domain.model.RecDomain
import com.example.lolaecu.domain.model.SaleRouteDomain
import com.example.lolaecu.ui.viewmodel.ConfigViewModel
import com.example.lolaecu.ui.viewmodel.QRPaymentViewModel
import com.example.lolaecu.ui.viewmodel.UtilsViewModel
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class PaymentFragment : Fragment() {

    private lateinit var barcodeView: DecoratedBarcodeView
    private val qrPaymentViewModel: QRPaymentViewModel by viewModels()
    private val utilsViewModel: UtilsViewModel by activityViewModels()
    private val configViewModel: ConfigViewModel by activityViewModels()

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
        initListeners()
        initQRScanner()
        initLolaObservers()
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

    private fun getFareRate(fares: List<Fares>): String {
        return try {
            val dayOfWeek = DateUtils.getDayOfWeek()
            val time = DateUtils.getTime()
            val fare = fares.find {
                it.days.contains(dayOfWeek)
                        &&
                        DateUtils.isTimeInRange(time, it.initHour, it.finishHour)
            }
            fare?.value ?: ""
        } catch (e: Exception) {
            Log.e("getFareRateException", e.stackTraceToString())
            ""
        }
    }

    private fun initLolaObservers() {
        //Observer that waits for a qr code String and then process the payment
        qrPaymentViewModel.qrString.observe(viewLifecycleOwner) { qrString ->
            if (isAvailableToProcessPayment()) {
                utilsViewModel.readingPaymentMethodStarted()
                val makePaymentRequest = buildPaymentRequestBody(
                    token = qrString,
                    paymentMethod = Constants.QR_PAYMENT_METHOD
                )
                qrPaymentViewModel.makeQrPayment(makePaymentRequest)
            }
        }
    }

    /** builds the payment request body for the API **/
    private fun buildPaymentRequestBody(
        token: String,
        paymentMethod: String
    ): MakeSaleRequest {
        val configuration: ConfigResponseModel = Configuration.getConfiguration()
        return try {
            MakeSaleRequest(
                imei = imei,
                rec = RecDomain(
                    amount = configViewModel.paymentRate.value ?: "",
                ),
                route = SaleRouteDomain(
                    routeShortName = configViewModel.routeName.value ?: "",
                    id = configuration.assign.route.routeId
                ),
                token = token,
                transactionType = when (paymentMethod) {
                    Constants.CARD_PAYMENT_METHOD -> Constants.CARD_PAYMENT_TYPE
                    Constants.QR_PAYMENT_METHOD -> Constants.QR_PAYMENT_TYPE
                    else -> {
                        ""
                    }
                },
                plotId = UUID.randomUUID().toString(),
                vehicle = configuration.assign.route.vehicleInternalNum,
                employeeFullName = configuration.assign.route.employeeFullName,
                driver = configuration.assign.route.employeeId.toString()
            )
        } catch (e: Exception) {
            Log.e("buildPaymentRequestBodyException", e.stackTraceToString())
            MakeSaleRequest()
        }
    }
    private fun initListeners() {
        try {
            configListener()
        } catch (e: Exception) {
            Log.e("initListenersException", e.stackTraceToString())
        }
    }
    private fun configListener() {
        try {
            configViewModel.configResponseLD.observe(viewLifecycleOwner) { response ->
                Log.i("telpoConfig", "config: $response")

                DateUtils.getDateInformation()
                val fares: List<Fares> = response.assign.fare[0].fares
                val routeRateString: String = getFareRate(fares) ?: ""
                val routeNameString: String = response.assign.route.routeShortName ?: ""
                configViewModel.setRouteName(routeNameString)
                configViewModel.setPaymentRate(routeRateString)
                //setUpUI()
            }
        } catch (e: Exception) {
            Log.e("configListenerException", e.stackTraceToString())
        }
    }

    /** validates if there's a valid rate and an assigned route so the payments can be processed **/
    private fun isAvailableToProcessPayment(): Boolean {
        return try {
            val paymentRate: String? = configViewModel.paymentRate.value
            val assignedRoute: String? = configViewModel.routeName.value
            //val isTourniquetOpen: Boolean = Tourniquet.getIsTourniquetOpen()

            (paymentRate.isNullOrBlank() ||
                    assignedRoute.isNullOrBlank() ||
                    TransactionStatus.isTransactionInProgress
                    ).not()
        } catch (e: Exception) {
            Log.e("isAvailableToProcessPaymentException", e.stackTraceToString())
            false
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