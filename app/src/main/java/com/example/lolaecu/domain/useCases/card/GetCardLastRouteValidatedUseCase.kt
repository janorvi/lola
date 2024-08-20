package com.example.lolaecu.domain.useCases.card

import com.example.lolaecu.core.utils.CardInformation
import javax.inject.Inject

class GetCardLastRouteValidatedUseCase @Inject constructor(

) {

    operator fun invoke(
        cardInformation: CardInformation.CardLastRouteValidated,
        block14: String
    ): String {

        val firstIndex: Int = cardInformation.indexes.first
        val secondsIndex: Int = cardInformation.indexes.second

        return if (block14.isNotBlank()) {
            block14.substring(firstIndex, secondsIndex).toLong(radix = 16).toString()
        } else {
            ""
        }

    }
}