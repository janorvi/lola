package com.example.lolaecu.ui.viewmodel

import com.example.lolaecu.core.utils.CardInformation

object CreditPaymentTest{

    fun modifyCardExpirationDate(
        block13: String,
        info: String, //yyMM 4bits
        cardInformation: CardInformation.CardExpirationDate
    ): String {
        return block13.replaceRange(
            cardInformation.indexes.first, cardInformation.indexes.second,
            info
        )
    }

    fun modifyCardStatus(
        block13: String,
        info: String,
        cardInformation: CardInformation.CardStatus //2 bits 00 / 01
    ): String {
        return block13.replaceRange(
            cardInformation.indexes.first, cardInformation.indexes.second,
            info
        )
    }
    fun modifyCardTranshipment(
        block13: String,
        info: String,
        cardInformation: CardInformation.CardTranshipment
    ): String {
        return block13.replaceRange(
            cardInformation.indexes.first, cardInformation.indexes.second,
            info
        )
    }
    fun modifyCardTranshipmentTime(
        block13: String,
        info: String,
        cardInformation: CardInformation.CardTranshipmentTime
    ): String {
        return block13.replaceRange(
            cardInformation.indexes.first, cardInformation.indexes.second,
            info
        )
    }

    fun modifyCardLastRouteValidated(
        block14: String,
        info: String,
        cardInformation: CardInformation.CardLastRouteValidated
    ): String {
        return block14.replaceRange(
            cardInformation.indexes.first, cardInformation.indexes.second,
            info
        )
    }

    fun modifyCardBalance(
        block12: String,
        info: String,
        cardInformation: CardInformation.CardTranshipmentTime
    ): String {
        return block12.replaceRange(
            cardInformation.indexes.first, cardInformation.indexes.second,
            info
        )
    }
    /*fun modifyGreenListItemBalance(
        greenListItem: GreenList,
        info: Int
    ): GreenList {
        greenListItem.balance = info
        return greenListItem
    }*/

    fun modifyCardCreditEnable(
        block12: String,
        info: String,
        cardInformation: CardInformation.CardCreditEnable
    ): String {
        return block12.replaceRange(
            cardInformation.indexes.first, cardInformation.indexes.second,
            info
        )
    }

    fun modifyCardCreditUse(
        block12: String,
        info: String,
        cardInformation: CardInformation.CardCreditUse
    ): String {
        return block12.replaceRange(
            cardInformation.indexes.first, cardInformation.indexes.second,
            info
        )
    }

}