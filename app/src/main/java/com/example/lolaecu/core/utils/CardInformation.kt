package com.example.lolaecu.core.utils

sealed class CardInformation() {
    data class CardUID(
        val blockNumber: String,
        val indexes: Pair<Int, Int>,
        val fieldSize: Int
    ) : CardInformation()

    data class CardBalance(
        val blockNumber: String,
        val indexes: Pair<Int, Int>,
        val fieldSize: Int
    ) : CardInformation()

    data class CardRtp(
        val blockNumber: String,
        val indexes: Pair<Int, Int>,
        val fieldSize: Int
    ) : CardInformation()

    data class CardCreditUse(
        val blockNumber: String,
        val indexes: Pair<Int, Int>,
        val fieldSize: Int
    ) : CardInformation()

    data class CardCreditEnable(
        val blockNumber: String,
        val indexes: Pair<Int, Int>,
        val fieldSize: Int
    ) : CardInformation()

    data class CardTranshipment(
        val blockNumber: String,
        val indexes: Pair<Int, Int>,
        val fieldSize: Int
    ) : CardInformation()

    data class CardTranshipmentTime(
        val blockNumber: String,
        val indexes: Pair<Int, Int>,
        val fieldSize: Int
    ) : CardInformation()
    data class CardProfile(
        val blockNumber: String,
        val indexes: Pair<Int, Int>,
        val fieldSize: Int
    ) : CardInformation()

    data class CardLastValidatorSerial(
        val blockNumber: String,
        val indexes: Pair<Int, Int>,
        val fieldSize: Int
    ) : CardInformation()

    data class CardStatus(
        val blockNumber: String,
        val indexes: Pair<Int, Int>,
        val fieldSize: Int
    ) : CardInformation()

    data class CardExpirationDate(
        val blockNumber: String,
        val indexes: Pair<Int, Int>,
        val fieldSize: Int
    ) : CardInformation()

    data class CardLastRouteValidated(
        val blockNumber: String,
        val indexes: Pair<Int, Int>,
        val fieldSize: Int
    ): CardInformation()
}


val cardUidInformation = CardInformation.CardUID(
    blockNumber = Constants.CARD_UID_BLOCK_NUMBER,
    indexes = Constants.CARD_UID_INDEXES,
    fieldSize = Constants.CARD_UID_FIELD_SIZE
)

val cardBalanceInformation = CardInformation.CardBalance(
    blockNumber = Constants.CARD_BALANCE_BLOCK_NUMBER,
    indexes = Constants.CARD_BALANCE_INDEXES,
    fieldSize = Constants.CARD_BALANCE_FIELD_SIZE
)

val cardRtpInformation = CardInformation.CardRtp(
    blockNumber = Constants.CARD_RTP_BLOCK_NUMBER,
    indexes = Constants.CARD_RTP_INDEXES,
    fieldSize = Constants.CARD_RTP_FIELD_SIZE
)

val cardCreditUseInformation = CardInformation.CardCreditUse(
    blockNumber = Constants.CARD_CREDIT_USE_BLOCK_NUMBER,
    indexes = Constants.CARD_CREDIT_USE_INDEXES,
    fieldSize = Constants.CARD_CREDIT_USE_FIELD_SIZE
)

val cardCreditEnableInformation = CardInformation.CardCreditEnable(
    blockNumber = Constants.CARD_CREDIT_ENABLE_BLOCK_NUMBER,
    indexes = Constants.CARD_CREDIT_ENABLE_INDEXES,
    fieldSize = Constants.CARD_CREDIT_ENABLE_FIELD_SIZE
)

val cardTranshipmentInformation = CardInformation.CardTranshipment(
    blockNumber = Constants.CARD_TRANSHIPMENT_BLOCK_NUMBER,
    indexes = Constants.CARD_TRANSHIPMENT_INDEXES,
    fieldSize = Constants.CARD_TRANSHIPMENT_FIELD_SIZE
)

val cardExpirationDateInformation = CardInformation.CardExpirationDate(
    blockNumber = Constants.CARD_EXPIRATION_DATE_BLOCK_NUMBER,
    indexes = Constants.CARD_EXPIRATION_DATE_INDEXES,
    fieldSize = Constants.CARD_EXPIRATION_DATE_FIELD_SIZE
)

val cardTranshipmentTimeInformation = CardInformation.CardTranshipmentTime(
    blockNumber = Constants.CARD_TRANSHIPMENT_TIME_BLOCK_NUMBER,
    indexes = Constants.CARD_TRANSHIPMENT_TIME_INDEXES,
    fieldSize = Constants.CARD_TRANSHIPMENT_TIME_FIELD_SIZE
)

val cardProfileInformation = CardInformation.CardProfile(
    blockNumber = Constants.CARD_PROFILE_BLOCK_NUMBER,
    indexes = Constants.CARD_PROFILE_INDEXES,
    fieldSize = Constants.CARD_PROFILE_FIELD_SIZE
)

val cardLastValidatorSerialInformation = CardInformation.CardLastValidatorSerial(
    blockNumber = Constants.CARD_LAST_VALIDATOR_SERIAL_BLOCK_NUMBER,
    indexes = Constants.CARD_LAST_VALIDATOR_SERIAL_INDEXES,
    fieldSize = Constants.CARD_LAST_VALIDATOR_SERIAL_FIELD_SIZE
)

val cardStatusInformation = CardInformation.CardStatus(
    blockNumber = Constants.CARD_STATUS_BLOCK_NUMBER,
    indexes = Constants.CARD_STATUS_INDEXES,
    fieldSize = Constants.CARD_STATUS_FIELD_SIZE
)

val cardLastRouteValidatedInformation = CardInformation.CardLastRouteValidated(
    blockNumber = Constants.CARD_LAST_ROUTE_VALIDATED_BLOCK_NUMBER,
    indexes = Constants.CARD_LAST_ROUTE_VALIDATED_INDEXES,
    fieldSize = Constants.CARD_LAST_ROUTE_VALIDATED_FIELD_SIZE
)

