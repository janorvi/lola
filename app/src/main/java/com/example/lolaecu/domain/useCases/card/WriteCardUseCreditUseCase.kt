package com.example.lolaecu.domain.useCases.card

import com.example.lolaecu.core.utils.CardInformation
import com.example.lolaecu.core.utils.Constants
import com.example.lolaecu.core.utils.DeviceInformation
import com.example.lolaecu.data.repository.NFCRepository
import javax.inject.Inject

class WriteCardUseCreditUseCase @Inject constructor(
    private val nfcRepository: NFCRepository
) {
    private val deviceModel = DeviceInformation.deviceModel
    operator fun invoke(
        cardInformation: CardInformation.CardCreditUse,
        informationToWrite: String
    ) {
        when(Constants.NFC_READER_TYPE){
            Constants.NFC_READER_TYPE_INTERNAL -> {
                when (deviceModel) {
                    Constants.TELPO_T20_DEVICE_MODEL -> nfcRepository.writeCardTPS530(
                        informationToWrite = informationToWrite,
                        blockNumber = cardInformation.blockNumber,
                        fieldIndexes = cardInformation.indexes,
                        fieldSize = cardInformation.fieldSize
                    )
                    Constants.TELPO_TPS530_DEVICE_MODEL -> nfcRepository.writeCardTPS530(
                        informationToWrite = informationToWrite,
                        blockNumber = cardInformation.blockNumber,
                        fieldIndexes = cardInformation.indexes,
                        fieldSize = cardInformation.fieldSize
                    )
                    Constants.TELPO_TPS320_DEVICE_MODEL -> nfcRepository.writeCardTPS530(
                        informationToWrite = informationToWrite,
                        blockNumber = cardInformation.blockNumber,
                        fieldIndexes = cardInformation.indexes,
                        fieldSize = cardInformation.fieldSize
                    )
                    else -> nfcRepository.writeCardTPS530(
                        informationToWrite = informationToWrite,
                        blockNumber = cardInformation.blockNumber,
                        fieldIndexes = cardInformation.indexes,
                        fieldSize = cardInformation.fieldSize
                    )
                }
            }
            Constants.NFC_READER_TYPE_EXTERNAL -> nfcRepository.writeCardTPS530(
                informationToWrite = informationToWrite,
                blockNumber = cardInformation.blockNumber,
                fieldIndexes = cardInformation.indexes,
                fieldSize = cardInformation.fieldSize
            )
            else -> nfcRepository.writeCardTPS530(
                informationToWrite = informationToWrite,
                blockNumber = cardInformation.blockNumber,
                fieldIndexes = cardInformation.indexes,
                fieldSize = cardInformation.fieldSize
            )
        }
    }
}