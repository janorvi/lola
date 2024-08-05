package com.example.lolaecu.ui.view

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lolaecu.core.utils.DeviceInformation
import com.example.lolaecu.core.utils.TransactionStatus
import com.example.lolaecu.databinding.FragmentFailurePaymentBinding
import com.example.lolaecu.ui.viewmodel.UtilsViewModel

class FailurePaymentFragment : Fragment() {

    private lateinit var binding: FragmentFailurePaymentBinding

    private val args: FailurePaymentFragmentArgs by navArgs()

    private var currentBalanceArg: String = ""
    private var transactionCodeArg: String = ""
    private var paymentMethodArg: String = ""
    private var transactionMessageArg: String = ""

    private val utilsViewModel:     UtilsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFailurePaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TransactionStatus.isTransactionInProgress = true
        utilsViewModel.readingPaymentMethodFinished()
        DeviceInformation.isCardBeingRead = false
        formatPriceStrings()
        paymentMethodArg = args.paymentMethod
        transactionCodeArg = args.transactionCode
        transactionMessageArg = args.transactionMessage
        setUpUI()
        comeBackToPaymentFragment()
    }

    private fun setUpUI() {
        when (transactionCodeArg) {
            "712" -> binding.apply {
                IVCardPayment?.visibility = View.VISIBLE
                TVCurrentBalance?.text = currentBalanceArg
            }
            "718" -> binding.apply {
                IVQrPayment?.visibility = View.VISIBLE
                TVCurrentBalance?.text = currentBalanceArg
            }
            "715" -> binding.apply {
                IVQrUsed?.visibility = View.VISIBLE
            }
            else -> binding.apply {
                IVGeneralError?.visibility = View.VISIBLE
                TVGeneralWarning?.text = transactionMessageArg
            }
        }
    }

    private fun formatPriceStrings() {
        try {
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
                val direction = FailurePaymentFragmentDirections
                    .actionFailurePaymentFragmentToPaymentFragment()
                findNavController().navigate(direction)
            }
        }
        timer.start()
    }
}