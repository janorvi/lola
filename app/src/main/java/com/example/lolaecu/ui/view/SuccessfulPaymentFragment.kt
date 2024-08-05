package com.example.lolaecu.ui.view

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lolaecu.core.utils.DeviceInformation
import com.example.lolaecu.core.utils.TransactionStatus
import com.example.lolaecu.databinding.FragmentSuccessfulPaymentBinding
import com.example.lolaecu.ui.viewmodel.QRPaymentViewModel
import com.example.lolaecu.ui.viewmodel.UtilsViewModel

class SuccessfulPaymentFragment : Fragment() {

    private lateinit var binding: FragmentSuccessfulPaymentBinding
    private val args: SuccessfulPaymentFragmentArgs by navArgs()
    private val utilsViewModel: UtilsViewModel by activityViewModels()

    private val qrPaymentViewModel: QRPaymentViewModel by viewModels()

    private var paymentRateArg: String = ""
    private var currentBalanceArg: String = ""
    private var paymentMethodArg: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSuccessfulPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TransactionStatus.isTransactionInProgress = true
        DeviceInformation.isCardBeingRead = false
        utilsViewModel.readingPaymentMethodFinished()
        paymentMethodArg = args.paymentMethod
        formatPriceStrings()
        setUpUI()

        comeBackToPaymentFragment()
    }

    /** setUp view's texts (prices) **/
    private fun setUpUI() {
        binding.apply {
            TVPaymentRate?.text = paymentRateArg
            TVCurrentBalance?.text = currentBalanceArg
        }
    }

    /** format price strings to display in the UI **/
    private fun formatPriceStrings() {
        try {
            paymentRateArg = if (args.paymentRate.isBlank()) {
                ""
            } else {
                "$ ${String.format("%,d", args.paymentRate.toInt())}"
            }

            currentBalanceArg = if (args.currentBalance.isBlank()) {
                ""
            } else {
                "$ ${String.format("%,d", args.currentBalance.toInt())}"
            }
        } catch (e: Exception) {
            Log.e("formatPriceStringsException", e.stackTraceToString())
        }
    }

    private fun comeBackToPaymentFragment() {
        /** CountDown timer to return to paymentFragment automatically **/
        val timer = object : CountDownTimer(3000, 1000) {
            override fun onTick(p0: Long) {
            }

            override fun onFinish() {
                val direction = SuccessfulPaymentFragmentDirections
                    .actionSuccessfulPaymentFragmentToPaymentFragment()
                findNavController().navigate(direction)
            }
        }
        timer.start()
    }
}