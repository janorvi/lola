package com.example.lolaecu.ui.view

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
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
import com.example.lolaecu.ui.viewmodel.CardPaymentViewModel
import com.example.lolaecu.ui.viewmodel.ConfigViewModel
import com.example.lolaecu.ui.viewmodel.QRPaymentViewModel
import com.example.lolaecu.ui.viewmodel.UtilsViewModel
import com.example.mdt.UserApplication
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

@AndroidEntryPoint
class PaymentFragment : Fragment() {

    private lateinit var barcodeView: DecoratedBarcodeView
    private val cardPaymentViewModel: CardPaymentViewModel by viewModels()
    private val qrPaymentViewModel: QRPaymentViewModel by viewModels()
    private val utilsViewModel: UtilsViewModel by activityViewModels()
    private val configViewModel: ConfigViewModel by activityViewModels()

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
        setUpUI()
        initListeners()
        initQRScanner()
        initLolaObservers()
        hideUIBars()
        binding.dateTextView.setOnClickListener {
            showUIBars()
        }
        //initLibraryObservers()
        //initPorts()
        //isPaymentMethodBeingReadListener()
        initFlows()
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
        //Observer that waits for a card UID and then process a payment
        cardPaymentViewModel.paymentCardUID.observe(viewLifecycleOwner) { cardUID ->
            if (isAvailableToProcessPayment()) {
                val makePaymentRequest = buildPaymentRequestBody(
                    token = cardUID,
                    paymentMethod = Constants.CARD_PAYMENT_METHOD
                )
                cardPaymentViewModel.makeCardPayment(makePaymentRequest)
            }
        }
        //Observer that waits for the QR payment response and then navigates to
        //the payment result fragment to display payment transaction response
        qrPaymentViewModel.qrPaymentResponse.observe(viewLifecycleOwner) { qrPaymentResponse ->

            val qrPaymentRate: String = getEmptyStringIfNull(qrPaymentResponse.data.amount)

            val qrCardBalance: String =
                getEmptyStringIfNull(qrPaymentResponse.data.balance)

            navigateToPaymentResultFragment(
                paymentMethod = Constants.QR_PAYMENT_METHOD,
                paymentRate = qrPaymentRate,
                currentBalance = qrCardBalance,
                errorMessage = qrPaymentResponse.message,
                transactionCode = qrPaymentResponse.code,
                paymentStatus = qrPaymentResponse.status
            )
        }
    }

    /** returns an empty string if the parameter received is null or the value as a string otherwise*/
    private fun getEmptyStringIfNull(value: Int?): String {
        return try {
            when (value == null) {
                true -> ""
                false -> value.toString()
            }
        } catch (e: Exception) {
            Log.e("getEmptyStringIfNullException", e.stackTraceToString())
            ""
        }
    }
    /** navigates to the payment result fragment with the given arguments  **/
    private fun navigateToPaymentResultFragment(
        paymentMethod: String,
        paymentRate: String,
        currentBalance: String,
        errorMessage: String,
        paymentStatus: String,
        transactionCode: String
    ) {
        try {
            val direction: NavDirections = when (paymentStatus) {
                Constants.TRANSACTION_SUCCESS -> PaymentFragmentDirections
                    .actionPaymentFragmentToSuccessfullPaymentFragment(
                        /* paymentRate = */ paymentRate,
                        /* currentBalance = */ currentBalance,
                        /* paymentMethod = */ paymentMethod
                    )

                Constants.TRANSACTION_DANGER -> PaymentFragmentDirections
                    .actionPaymentFragmentToFailurePaymentFragment(
                        /* currentBalance = */ currentBalance,
                        /* transactionCode = */ transactionCode,
                        /* paymentMethod = */ paymentMethod,
                        /* transactionMessage = */ errorMessage
                    )

                Constants.TRANSACTION_WARNING -> PaymentFragmentDirections
                    .actionPaymentFragmentToWarningPaymentFragment(
                        /* transactionCode = */ transactionCode,
                        /* transactionMessage = */ errorMessage
                    )

                Constants.TRANSACTION_INFO -> PaymentFragmentDirections
                    .actionPaymentFragmentToFailurePaymentFragment(
                        /* currentBalance = */ currentBalance,
                        /* transactionCode = */ transactionCode,
                        /* paymentMethod = */ paymentMethod,
                        /* transactionMessage = */ errorMessage
                    )

                else -> PaymentFragmentDirections
                    .actionPaymentFragmentToWarningPaymentFragment(
                        /* transactionCode = */ transactionCode,
                        /* transactionMessage = */ errorMessage
                    )
            }
            findNavController().navigate(direction)
        } catch (e: Exception) {
            Log.e("navigateToPaymentResultFragment", e.stackTraceToString())
        }
    }
    /** builds the payment request body for the API **/
    private fun buildPaymentRequestBody(
        token: String,
        paymentMethod: String
    ): MakeSaleRequest {
        val configuration: ConfigResponseModel = Configuration.getConfiguration()
        return try {
            val s = UserApplication.prefs.getStorage(Constants.MDT_IMEI)
            MakeSaleRequest(
                imei = s,
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
    private fun setUpUI() {
        try {
            val configRouteName = configViewModel.routeName.value ?: "N/A"
            val configRouteRate = configViewModel.paymentRate.value ?: "N/A"
            binding.apply {
                if (configRouteName.isBlank()) {
                    noRouteScreen?.visibility = View.VISIBLE
                    routeName.visibility = View.GONE
                    routeNameTitle.visibility = View.GONE
                    routeRate.visibility = View.GONE
                    //paymentProcessingScreen?.visibility = View.GONE
                } else if (isPaymentMethodBeingRead) {
                    //paymentProcessingScreen?.visibility = View.VISIBLE
                    routeName.visibility = View.GONE
                    routeNameTitle.visibility = View.GONE
                    routeRate.visibility = View.GONE
                } else {
                    if (DeviceInformation.deviceModel != Constants.TELPO_T20_DEVICE_MODEL) {
                        barcodeView.visibility = View.VISIBLE
                    }
                    //paymentProcessingScreen?.visibility = View.GONE
                    noRouteScreen?.visibility = View.GONE
                    routeName.text = configRouteName
                    routeRate.text = if (configRouteRate.isBlank()) {
                        "Sin Tarifa"
                    } else {
                        "$${String.format("%.2f", configRouteRate.toDouble())}"
                    }
                    routeName.visibility = View.VISIBLE
                    routeNameTitle.visibility = View.VISIBLE
                    routeRate.visibility = View.VISIBLE
                }
            }
        } catch (e: Exception) {
            Log.e("setUpUIException", e.stackTraceToString())
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
                setUpUI()
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

    private fun hideUIBars() {
        val intent = Intent("android.intent.action.systemui")
        intent.putExtra("navigation_bar", "off")
        intent.putExtra("status_bar", "off")
        intent.putExtra("statusbar_drop", "off")
        intent.putExtra("qsTitle", "off")
        requireContext().sendBroadcast(intent)

        val intentkey = Intent("android.intent.action.touchkey")
        intentkey.putExtra("recent_key", "off")
        intentkey.putExtra("home_key", "off")
        intentkey.putExtra("back_key", "off")
        intentkey.putExtra("menu_key", "off")
        intentkey.putExtra("bar_key", "off")
        requireContext().sendBroadcast(intentkey)
    }

    private fun showUIBars() {
        val intent = Intent("android.intent.action.systemui")
        intent.putExtra("navigation_bar", "on")
        intent.putExtra("status_bar", "on")
        intent.putExtra("statusbar_drop", "on")
        intent.putExtra("qsTitle", "on")
        requireContext().sendBroadcast(intent)

        val intentkey = Intent("android.intent.action.touchkey")
        intentkey.putExtra("recent_key", "on")
        intentkey.putExtra("home_key", "on")
        intentkey.putExtra("back_key", "on")
        intentkey.putExtra("menu_key", "on")
        intentkey.putExtra("bar_key", "on")
        requireContext().sendBroadcast(intentkey)
    }

    private fun initFlows() {
        cardPaymentViewModel.initCardReadingFlow(Intent())
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