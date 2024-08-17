package com.example.lolaecu.domain.useCases.card

import com.example.lolaecu.core.utils.CardInformation
import com.example.lolaecu.data.repository.NFCRepository
import javax.inject.Inject

class GetCardBalanceUseCase @Inject constructor(
    private val nfcRepository: NFCRepository
) {

    operator fun invoke(
        cardInformation: CardInformation.CardBalance,
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