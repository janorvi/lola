package com.example.lolaecu.domain.useCases.card

import android.content.Intent
import com.example.lolaecu.core.utils.Constants
import com.example.lolaecu.core.utils.DeviceInformation
import com.example.lolaecu.data.model.CardBlocksModel
import com.example.lolaecu.data.repository.NFCRepository
import javax.inject.Inject

class ReadCardBlocksUseCase @Inject constructor(
    private val nfcRepository: NFCRepository
) {
    private val deviceModel = DeviceInformation.deviceModel

    operator fun invoke(intent: Intent): CardBlocksModel {
        return when(Constants.NFC_READER_TYPE){
            Constants.NFC_READER_TYPE_INTERNAL -> {
                when (deviceModel) {
                    Constants.TELPO_T20_DEVICE_MODEL -> nfcRepository.readCardTPS530()
                    Constants.TELPO_TPS530_DEVICE_MODEL -> nfcRepository.readCardTPS530()
                    Constants.TELPO_TPS320_DEVICE_MODEL -> nfcRepository.readCardTPS530()
                    else -> nfcRepository.readCardTPS530()
                }
            }
            Constants.NFC_READER_TYPE_EXTERNAL -> {
                nfcRepository.readCardTPS530()
            }
            else -> {
                nfcRepository.readCardTPS530()
            }
        }
    }
}