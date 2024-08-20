package com.example.lolaecu.domain.useCases.card

import com.example.lolaecu.core.utils.CardInformation
import javax.inject.Inject

class GetCardStatusUseCase @Inject constructor(

) {
    operator fun invoke(
        cardInformation: CardInformation.CardStatus,
        block13: String
    ): String {

        val firstIndex: Int = cardInformation.indexes.first
        val secondsIndex: Int = cardInformation.indexes.second

        return if (block13.isNotBlank()) {
            block13.substring(firstIndex, secondsIndex).toLong(radix = 16).toString()
        } else {
            ""
        }
    }
}