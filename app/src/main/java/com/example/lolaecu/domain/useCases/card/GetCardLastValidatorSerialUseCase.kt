package com.example.lolaecu.domain.useCases.card

import com.example.lolaecu.core.utils.CardInformation
import javax.inject.Inject

class GetCardLastValidatorSerialUseCase @Inject constructor(

) {
    operator fun invoke(
        cardInformation: CardInformation.CardLastValidatorSerial,
        block12: String
    ): String {

        val firstIndex: Int = cardInformation.indexes.first
        val secondsIndex: Int = cardInformation.indexes.second

        return if (block12.isNotBlank()) {
            block12.substring(firstIndex, secondsIndex).toLong(radix = 16).toString()
        } else {
            ""
        }

    }
}