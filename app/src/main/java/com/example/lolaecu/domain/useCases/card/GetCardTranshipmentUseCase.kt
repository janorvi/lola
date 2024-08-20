package com.example.lolaecu.domain.useCases.card

import com.example.lolaecu.core.utils.CardInformation
import javax.inject.Inject

class GetCardTranshipmentUseCase @Inject constructor(

) {
    operator fun invoke(
        cardInformation: CardInformation.CardTranshipment,
        block13: String
    ): String {
        val firstIndex: Int = cardInformation.indexes.first
        val secondIndex: Int = cardInformation.indexes.second

        return if (block13.isNotBlank()) {
            block13.substring(firstIndex, secondIndex).toLong(radix = 16).toString()
        } else {
            ""
        }
    }
}