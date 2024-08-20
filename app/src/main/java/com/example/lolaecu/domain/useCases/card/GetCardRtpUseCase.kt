package com.example.lolaecu.domain.useCases.card

import com.example.lolaecu.core.utils.CardInformation
import com.example.lolaecu.data.repository.NFCRepository
import javax.inject.Inject

class GetCardRtpUseCase @Inject constructor(
    private val nfcRepository: NFCRepository
) {
    operator fun invoke(
        cardInformation: CardInformation.CardRtp,
        block12: String
    ): String {

        val firstIndex: Int = cardInformation.indexes.first
        val secondIndex: Int = cardInformation.indexes.second

        return if (block12.isNotEmpty()) {
            block12.substring(firstIndex, secondIndex).toLong(radix = 16).toString()
        } else {
            ""
        }
    }
}