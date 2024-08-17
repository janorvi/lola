package com.example.lolaecu.domain.useCases.card

import android.util.Log
import com.example.lolaecu.core.utils.CardInformation
import com.example.lolaecu.core.utils.DeviceInformation
import com.example.lolaecu.data.repository.NFCRepository
import javax.inject.Inject

class GetCardUidUseCase @Inject constructor(
    private val nfcRepository: NFCRepository
) {
    private val deviceModel = DeviceInformation.deviceModel
    /** return the cardUID (token) as Hexadecimal in a String **/
    operator fun invoke(
        cardInformation: CardInformation.CardUID,
        block14: String
    ): String {
        Log.i("DeviceModel", deviceModel)

        val firstIndex = cardInformation.indexes.first
        val secondIndex = cardInformation.indexes.second

        return if (block14.isNotEmpty()) {
            block14.substring(firstIndex,secondIndex)
        }  else {
            ""
        }

//        return when (deviceModel) {
//            Constants.TELPO_TPS530_DEVICE_MODEL -> nfcRepository.readCardD135(
//                blockNumber = cardInformation.blockNumber,
//                fieldIndexes = cardInformation.indexes
//            )
//
//            Constants.TELPO_T20_DEVICE_MODEL -> nfcRepository.readCardD135(
//                blockNumber = cardInformation.blockNumber,
//                fieldIndexes = cardInformation.indexes
//            )
//
//            Constants.TELPO_TPS320_DEVICE_MODEL -> nfcRepository.readCardD135(
//                blockNumber = cardInformation.blockNumber,
//                fieldIndexes = cardInformation.indexes
//            )
//            else -> {
//                nfcRepository.nxpNFCReader(intent)
//            }
//        }
    }
}