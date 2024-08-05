package com.example.lolaecu.ui.view

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lolaecu.core.utils.DeviceInformation
import com.example.lolaecu.core.utils.TransactionStatus
import com.example.lolaecu.databinding.FragmentWarningPaymentBinding
import com.example.lolaecu.ui.viewmodel.UtilsViewModel

class WarningPaymentFragment : Fragment() {

    private lateinit var binding: FragmentWarningPaymentBinding
    private val args: WarningPaymentFragmentArgs by navArgs()

    private var transactionCodeArg: String = ""
    private var transactionMessageArg: String = ""
    private val utilsViewModel: UtilsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWarningPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TransactionStatus.isTransactionInProgress = true
        utilsViewModel.readingPaymentMethodFinished()
        DeviceInformation.isCardBeingRead = false
        transactionCodeArg = args.transactionCode
        transactionMessageArg = args.transactionMessage
        setUpUI()
        comeBackToPaymentFragment()
    }

    private fun setUpUI() {
        when (transactionCodeArg) {
            "715" -> binding.IVQrUsed?.visibility = View.VISIBLE
            else -> binding.apply {
                IVGeneralWarning?.visibility = View.VISIBLE
                TVGeneralWarning?.text = transactionMessageArg
            }
        }
    }

    private fun comeBackToPaymentFragment() {
        /** CountDown timer to return to paymentFragment automatically **/
        val timer = object : CountDownTimer(3000, 1000) {
            override fun onTick(p0: Long) {
            }

            override fun onFinish() {
                val direction = WarningPaymentFragmentDirections
                    .actionWarningPaymentFragmentToPaymentFragment()
                findNavController().navigate(direction)
            }
        }
        timer.start()
    }
}