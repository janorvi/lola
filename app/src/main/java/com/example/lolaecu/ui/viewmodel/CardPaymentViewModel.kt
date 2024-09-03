package com.example.lolaecu.ui.viewmodel

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lolaecu.core.utils.CardBlocks
import com.example.lolaecu.core.utils.Configuration
import com.example.lolaecu.core.utils.Constants
import com.example.lolaecu.core.utils.DateUtils
import com.example.lolaecu.core.utils.DeviceInformation
import com.example.lolaecu.core.utils.DeviceInformationUtils
import com.example.lolaecu.core.utils.PaymentResponseBuilder
import com.example.lolaecu.core.utils.PaymentTransaction
import com.example.lolaecu.core.utils.RtpUtils
import com.example.lolaecu.core.utils.TransactionStatus
import com.example.lolaecu.core.utils.cardBalanceInformation
import com.example.lolaecu.core.utils.cardCreditEnableInformation
import com.example.lolaecu.core.utils.cardCreditUseInformation
import com.example.lolaecu.core.utils.cardExpirationDateInformation
import com.example.lolaecu.core.utils.cardLastRouteValidatedInformation
import com.example.lolaecu.core.utils.cardLastValidatorSerialInformation
import com.example.lolaecu.core.utils.cardProfileInformation
import com.example.lolaecu.core.utils.cardRtpInformation
import com.example.lolaecu.core.utils.cardStatusInformation
import com.example.lolaecu.core.utils.cardTranshipmentInformation
import com.example.lolaecu.core.utils.cardTranshipmentTimeInformation
import com.example.lolaecu.core.utils.cardUidInformation
import com.example.lolaecu.data.model.CardBlocksModel
import com.example.lolaecu.data.model.Fare
import com.example.lolaecu.data.model.Fares
import com.example.lolaecu.data.model.MakeSaleResponseModel
import com.example.lolaecu.data.model.NetworkResult
import com.example.lolaecu.data.model.OnlineCardPaymentUseCase
import com.example.lolaecu.data.model.Transhipments
import com.example.lolaecu.data.repository.FramesRepository
import com.example.lolaecu.data.repository.NFCRepository
import com.example.lolaecu.data.repository.TransactionRepository
import com.example.lolaecu.domain.model.MakeSaleRequest
import com.example.lolaecu.domain.model.MakeSaleResponse
import com.example.lolaecu.domain.model.toDomain
import com.example.lolaecu.domain.useCases.card.GetCardBalanceUseCase
import com.example.lolaecu.domain.useCases.card.GetCardCreditEnableUseCase
import com.example.lolaecu.domain.useCases.card.GetCardExpirationDateUseCase
import com.example.lolaecu.domain.useCases.card.GetCardLastRouteValidatedUseCase
import com.example.lolaecu.domain.useCases.card.GetCardLastValidatorSerialUseCase
import com.example.lolaecu.domain.useCases.card.GetCardProfileUseCase
import com.example.lolaecu.domain.useCases.card.GetCardRtpUseCase
import com.example.lolaecu.domain.useCases.card.GetCardStatusUseCase
import com.example.lolaecu.domain.useCases.card.GetCardTranshipmentTimeUseCase
import com.example.lolaecu.domain.useCases.card.GetCardTranshipmentUseCase
import com.example.lolaecu.domain.useCases.card.GetCardUidUseCase
import com.example.lolaecu.domain.useCases.card.GetCardUsedCreditUseCase
import com.example.lolaecu.domain.useCases.card.ReadCardBlocksUseCase
import com.example.lolaecu.domain.useCases.card.WriteCardTranshipmentUseCase
import com.example.lolaecu.domain.useCases.card.WriteCardUseCreditUseCase
import com.example.lolaecu.domain.useCases.transactions.ResendTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardPaymentViewModel @Inject constructor(
    private val getCardUidUseCase: GetCardUidUseCase,
    private val nfcRepository: NFCRepository,
    private val onlineCardPaymentUseCase: OnlineCardPaymentUseCase,
    private val transactionRepository: TransactionRepository,
    private val resendTransactionUseCase: ResendTransactionUseCase,
    private val framesRepository: FramesRepository,
    private val getCardBalanceUseCase: GetCardBalanceUseCase,
    private val readCardBlocksUseCase: ReadCardBlocksUseCase,
    private val getCardRtpUseCase: GetCardRtpUseCase,
    private val getCardExpirationDate: GetCardExpirationDateUseCase,
    private val getCardProfile: GetCardProfileUseCase,
    private val getCardTranshipmentUseCase: GetCardTranshipmentUseCase,
    private val getCardTranshipmentTimeUseCase: GetCardTranshipmentTimeUseCase,
    private val getCardLastValidatorSerialUseCase: GetCardLastValidatorSerialUseCase,
    private val getCardStatusUseCase: GetCardStatusUseCase,
    private val getCardLastRouteValidatedUseCase: GetCardLastRouteValidatedUseCase,
    private val getCardCreditEnableUseCase: GetCardCreditEnableUseCase,
    private val getCardCreditUsedUseCase: GetCardUsedCreditUseCase,
    private val deviceInformationUtils: DeviceInformationUtils,
    private val writeCardTranshipmentUseCase: WriteCardTranshipmentUseCase,
    private val writeCardUseCreditUseCase: WriteCardUseCreditUseCase
) : ViewModel() {
    private var _isNFCReaderConnected: MutableLiveData<String> = MutableLiveData()
    val isNFCReaderConnected: LiveData<String> = _isNFCReaderConnected

    private var _cardPaymentResponse: MutableLiveData<MakeSaleResponse> = MutableLiveData()
    val cardPaymentResponse: LiveData<MakeSaleResponse> = _cardPaymentResponse

    private val _paymentCardUID: MutableLiveData<String> = MutableLiveData()
    val paymentCardUID: LiveData<String> = _paymentCardUID

    private var _isCardBeingReadLD: MutableLiveData<Boolean> = MutableLiveData(false)
    val isCardBeingRead: LiveData<Boolean> = _isCardBeingReadLD

    /** Flow that executes the card reading implementation every n seconds **/
    fun initCardReadingFlow(intent: Intent) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                nfcRepository
                    .readMifareNFCFlow
                    .collect {
                        if (it && !TransactionStatus.isTransactionInProgress) {
                            val cardBlocks: CardBlocksModel = readCardBlocksUseCase(intent)
                            val block14: String = cardBlocks.block14
                            //Get card UID
                            val cardUID: String = getCardUidUseCase(
                                cardInformation = cardUidInformation,
                                block14 = block14
                            )
                            if (cardUID.isNotEmpty()) {
                                Log.i("initCardReadingFlow", "cardUID: $cardUID")
                                getCardInformationFromBlocks()
                                _paymentCardUID.postValue(cardUID)
                            }
                        }
                    }
            } catch (e: Exception) {
                Log.e("initCardReadingFlowException", e.stackTraceToString())
            }
        }
    }

    /** function for debugging only **/
    private fun getCardInformationFromBlocks() {
        //Get card balance
        val block12: String = CardBlocks.block12
        val block13: String = CardBlocks.block13
        val block14: String = CardBlocks.block14

        val cardBalance: String =
            getCardBalanceUseCase(
                cardInformation = cardBalanceInformation,
                block12 = block12
            )

        //Get card RTP
        val cardRTP: String = getCardRtpUseCase(
            cardInformation = cardRtpInformation,
            block12 = block12
        )
        //Get card profile
        val cardProfile: String =
            getCardProfile(
                cardInformation = cardProfileInformation,
                block12 = block12
            )
        //Get card transhipment
        val cardTranshipment: String = getCardTranshipmentUseCase(
            cardInformation = cardTranshipmentInformation, block13 = block13
        )
        //Get card transhipment time
        val cardTranshipmentTime: String = getCardTranshipmentTimeUseCase(
            cardInformation = cardTranshipmentTimeInformation, block13 = block13
        )
        //Get card last validator serial
        val cardLastValidatorSerial: String = getCardLastValidatorSerialUseCase(
            cardInformation = cardLastValidatorSerialInformation,
            block12 = block12
        )
        //Get card status
        val cardStatus: String = getCardStatusUseCase(
            cardInformation = cardStatusInformation, block13 = block13
        )
        //Get card last route validated
        val lastRouteValidated: String = getCardLastRouteValidatedUseCase(
            cardInformation = cardLastRouteValidatedInformation,
            block14 = block14
        )
        //Get card credit enable
        val cardCreditEnable: String = getCardCreditEnableUseCase(
            cardInformation = cardCreditEnableInformation, block12 = block12
        )
        //Get card credit use
        val cardCreditUse: String = getCardCreditUsedUseCase(
            cardInformation = cardCreditUseInformation, block12 = block12
        )
        Log.i("initCardReadingFlow", "cardBalance: $cardBalance")
        Log.i("initCardReadingFlow", "cardRTP: $cardRTP")
        Log.i("initCardReadingFlow", "cardProfile: $cardProfile")
        Log.i("initCardReadingFlow", "cardTranshipment: $cardTranshipment")
        Log.i(
            "initCardReadingFlow",
            "cardTranshipmentTime: $cardTranshipmentTime"
        )
        Log.i(
            "initCardReadingFlow",
            "cardLastValidatorSerial: $cardLastValidatorSerial"
        )
        Log.i("initCardReadingFlow", "cardStatus: $cardStatus")
        Log.i("initCardReadingFlow", "lastRouteValidated: $lastRouteValidated")
        Log.i("initCardReadingFlow", "cardCreditEnable: $cardCreditEnable")
        Log.i("initCardReadingFlow", "cardCreditUse: $cardCreditUse")
    }

    /** validate if the card is expired by comparing card expiration date
     * with current time **/
    private fun compareCardExpirationDateWithCurrentDate(cardExpirationDate: String): Boolean {
        return try {
            val cardExpirationDateInt: Int = cardExpirationDate.toInt()
            val currentDate: Int = RtpUtils.getCurrentDateYYMM().toInt()
            cardExpirationDateInt <= currentDate
        } catch (e: Exception) {
            Log.e("validateIsCardExpiredException", e.stackTraceToString())
            false
        }
    }

    /** validate if the card status == 1 (active) **/
    private fun validateCardStatus(cardStatus: String): Boolean {
        return try {
            val cardStatusInt: Int = cardStatus.toInt()
            return cardStatusInt == 1
        } catch (e: Exception) {
            Log.e("isCardActiveException", e.stackTraceToString())
            false
        }
    }

    /** get the fares by card profile. Information on configuration fares **/
    private fun getCardFaresByProfile(cardProfile: String): Fares? {
        return try {
            val cardProfileInt: Int = cardProfile.toInt()
            val dayOfWeek: String = DateUtils.getDayOfWeek()
            val time: String = DateUtils.getTime()

            val fare: List<Fare> =
                Configuration.getConfiguration().assign.fare

            val profileFare: Fare = fare.find { it.id == cardProfileInt } ?: Fare()

            val profileFaresByTime: Fares? = profileFare.fares.find {
                it.days.contains(dayOfWeek) &&
                        DateUtils.isTimeInRange(
                            time,
                            it.initHour,
                            it.finishHour
                        )
            }
            Log.i("validateTranshipment", "profileFaresByTime: $profileFaresByTime")
            return profileFaresByTime
        } catch (e: Exception) {
            Log.e("getCardPaymentRateByProfileException", e.stackTraceToString())
            Fares()
        }
    }

    /** validate if the current route is a valid transhipment based on the previous route
     * the card was used on, information validated on the configuration fares **/
    private fun isTranshipmentValid(
        lastRouteValidated: String,
        cardProfile: String
    ): Boolean {
        val currentRouteId: Int =
            Configuration.getConfiguration().assign.route.routeId.toInt() ?: 0
        val currentRouteIdMock: Int = 50
        val profileFares: Fares = getCardFaresByProfile(cardProfile) ?: return false
        val previousRouteValidatedTranshipment: Transhipments = profileFares.transhipments.find {
            it.sourcePath == lastRouteValidated.toInt()
        } ?: Transhipments()
        return previousRouteValidatedTranshipment.routesDestinations.contains(
            currentRouteId,
//            currentRouteIdMock
        )
    }

    /** validate if the minimum time for transhipment has passed, based on the card profile and the
     *  information from the configuration fares **/
    private fun isTranshipmentTimeValid(cardShortRtp: String): Boolean {
        val minimumTimeForTranshipment: Int = Configuration.getConfiguration().timeForTranshipment
        val currentDate: String = RtpUtils.getShortRtp()

        //Mocks
        val currentDateMock: String = "240116101234" //10:12:34 mock
        val cardShortRtpMock: String = "240116100123" //10:01:23

        val timeDiffInMinutes: Int = DateUtils.getTimeDifferenceInMinutes(
            fromTime = cardShortRtp,
            toTime = currentDate

//            fromTime = cardShortRtpMock,
//            toTime = currentDateMock
        )
        return timeDiffInMinutes >= minimumTimeForTranshipment
    }

    /** get the card transhipment rate from configuration fares **/
    private fun getTranshipmentPaymentRate(
        cardFares: Fares
    ): Double {
        return cardFares.transshipmentPay.toDouble()
    }

    /** validate if transaction is a transhipment and if it's valid **/
    private fun isTranshipmentRouteAndTimeValidByCardProfile(
        cardProfile: String,
        cardRtp: String,
        //greenListItem: GreenList
    ): Boolean {
        return try {

            val block13: String = CardBlocks.block13
            val mockCardTranshipment: String = CreditPaymentTest.modifyCardTranshipment(
                block13 = block13,
                info = "01",
                cardInformation = cardTranshipmentInformation
            )

            val cardTranshipment: Int = getCardTranshipmentUseCase(
                cardInformation = cardTranshipmentInformation,
                block13 = CardBlocks.block13
//                block13 = mockCardTranshipment
            ).toInt()

            //val greenListRtp: String = greenListItem.rtp

            //Get the information from the most updated rtp (card or GL)
            var mostUpdatedShortRtp: String
            var mostUpdatedTranshipmentEnabled: Int

            //if (cardRtp.toLong() >= greenListRtp.toLong()) {
                    mostUpdatedShortRtp = cardRtp
                    mostUpdatedTranshipmentEnabled = cardTranshipment
            /*} else {
                    mostUpdatedShortRtp = RtpUtils.convertLongToShortRTP(greenListRtp)
                    mostUpdatedTranshipmentEnabled = greenListItem.pc
            }*/

            Log.i("validateTranshipment", "cardTranshipment: $cardTranshipment")

            //If is transhipment
            if (mostUpdatedTranshipmentEnabled == 1) {
                val lastRouteValidated: String = getCardLastRouteValidatedUseCase(
                    cardInformation = cardLastRouteValidatedInformation,
                    block14 = CardBlocks.block14
                )

                //last route validated mock
                val lastRouteValidatedMock: String = "10" //10 = R100

                val isTranshipmentValid: Boolean = isTranshipmentValid(
                    lastRouteValidated = lastRouteValidated,
//                    lastRouteValidated = lastRouteValidatedMock,
                    cardProfile = cardProfile
                )
                val isTranshipmentTimeValid: Boolean = isTranshipmentTimeValid(
                    cardShortRtp = mostUpdatedShortRtp
                )
                Log.i("validateTranshipment", "lastRouteValidated: $lastRouteValidated")
                Log.i("validateTranshipment", "isTranshipmentValid: $isTranshipmentValid")
                Log.i("validateTranshipment", "isTranshipmentTimeValid: $isTranshipmentTimeValid")

                //if the transhipment is valid, write the card transhipment back to 0
                if (isTranshipmentValid && isTranshipmentTimeValid) {
                    writeCardTranshipmentUseCase(
                        cardInformation = cardTranshipmentInformation,
                        informationToWrite = "00"
                    )
                }

                //If transhipment is valid and has passed the correct time (based on configuration data)
                (isTranshipmentValid && isTranshipmentTimeValid)
            } else {
                //not a transhipment
                false
            }
        } catch (e: Exception) {
            Log.e("isTranshipmentRouteAndTimeValidByCardProfileException", e.stackTraceToString())
            false
        }
    }

    /** validate if the credit can be processed following the credit payment flow
     * validating configuration ctype, validator connection to internet and card credit statuses **/
    private suspend fun processCreditPaymentIfValid(
        cardPaymentRequestBody: MakeSaleRequest,
        cardRtp: Long,
        //greenListItem: GreenList
    ) {
        try {
            /** 7) evaluate validator network status **/
            //mock validator not connected
            val isValidatorConnectedToInternet: Boolean =
                deviceInformationUtils.isNetworkAvailable()
//            val isValidatorConnectedToInternet = false
            Log.i(
                "validateDeviceConnection",
                "isValidatorConnectedToInternet $isValidatorConnectedToInternet"
            )

            //build the "no balance" response
            val noBalanceCardPaymentResponse: MakeSaleResponse =
                PaymentResponseBuilder.buildNoBalanceResponse(
                    balance = cardPaymentRequestBody.rec.balance.toInt(),
                    paymentRate = cardPaymentRequestBody.rec.amount.toInt()
                )
            //if validator is connected
            if (isValidatorConnectedToInternet) {
                //post noBalance payment response
                _cardPaymentResponse.postValue(noBalanceCardPaymentResponse)
            } else { //else the validator is not connected to internet

                /** 10) validate cType **/
                //Mock ctype
                val validatorCtype: Int = getValidatorCreditAuthorizationType()
//                val validatorCtype: Int = 0
                Log.i("validateCredit", "validatorCtype $validatorCtype")

                //if ctype is 1 (offline) or 3 (offline or online) the validator is able to
                //process credit payment
                val isValidatorAuthorizedToProcessCredit: Boolean = (validatorCtype == 1 ||
                        validatorCtype == 3)

                //if validator is authorized to process credit payments
                if (isValidatorAuthorizedToProcessCredit) {
                    //validates if the credit is valid
                    val isCreditOfflineValid = isCreditOfflineValid(
                        cardRtp = cardRtp,
                        //greenListItem = greenListItem
                    )
                    Log.i("validateCredit", "isCreditOfflineValid $isCreditOfflineValid")

                    //if the credit is valid
                    if (isCreditOfflineValid) {
                        //write card credit use to "01" to set the credit has been used
                        writeCardUseCreditUseCase(
                            cardInformation = cardCreditUseInformation,
                            informationToWrite = "01"
                        )

                        //when the offline is set to one, backend understand it as a credit payment
                        cardPaymentRequestBody.offline = "1"

                        //process the payment as online or offline depending on configurations
                        when (!DeviceInformation.isTransactionsOffline) {
                            false -> makeOnlinePayment(cardPaymentRequestBody)
                            true -> makeOnlinePayment(cardPaymentRequestBody)
                        }
                    } else { //
                        //post no balance payment response since credit was not valid (either
                        // credit was not enabled oe credit was already used)
                        _cardPaymentResponse.postValue(noBalanceCardPaymentResponse)
                    }
                } else { //the validator is not authorized for processing credits
                    //post no balance payment response since validator was not authorized
                    //to process credit
                    _cardPaymentResponse.postValue(noBalanceCardPaymentResponse)
                }
            }
        } catch (e: Exception) {
            Log.e("processCreditPaymentIfValidException", e.stackTraceToString())
        }
    }

    /** validate if the card is enable for credit sales and has credit uses remaining **/
    private fun validateCardCredit(cardCreditEnable: String, cardCreditUse: String): Boolean {
        return try {
            val cardCreditEnableInt: Int = cardCreditEnable.toInt()
            val cardCreditUseInt: Int = cardCreditUse.toInt()
            //return
            (cardCreditEnableInt == 1 && cardCreditUseInt == 0)
        } catch (e: Exception) {
            Log.e("validateCardCreditException", e.stackTraceToString())
            false
        }
    }

    /** return the Ctype value from the configuration
     * case 0 = unauthorized for credits
     * case 1 = authorized when validator is offline
     * case 2 = authorized when validator is online
     * case 3 = authorized when validator is either online or offline **/
    private fun getValidatorCreditAuthorizationType(): Int {
        return Configuration.getConfiguration().ctype
    }

    /** If validator is offline and the card RTP is higher than GL's, validates if the credit
     * payment may be processed, otherwise verifies credit enable on GL **/
    private suspend fun isCreditOfflineValid(
        cardRtp: Long,
        //greenListItem: GreenList
    ): Boolean {
        return try {
            val block12: String = CardBlocks.block12
            //val greenListShortRtp: Long = RtpUtils.convertLongToShortRTP(greenListItem.rtp).toLong()
            //val greenListCreditEnable: Int = greenListItem.credit

            /** 8) evaluate card credit enabled **/
            /** 9) evaluate card credit use **/
            val cardCreditUse: String = getCardCreditUsedUseCase(
                cardInformation = cardCreditUseInformation,
                block12 = block12
            )

            val cardCreditEnable: String = getCardCreditEnableUseCase(
                cardInformation = cardCreditEnableInformation, block12 = block12
            )

            //Mocks
//            val cardCreditUse: String = "00" // 00 disponible, 01 no disponible
//            val cardCreditEnable: String = "01" //00 not active, 01 active

            Log.i("validateCredit", "cardCreditUse $cardCreditUse")
            Log.i("validateCredit", "cardCreditEnable $cardCreditEnable")
            Log.i("validateCredit", "cardRtp $cardRtp")
            //Log.i("validateCredit", "greenListShortRtp $greenListShortRtp")
            //Log.i("validateCredit", "greenListCreditEnable $greenListCreditEnable")

            //validates the greater RTP between card's and GL's
            /*if (cardRtp >= greenListShortRtp) {

                //validate card credit use and credit enable
                validateCardCredit(
                    cardCreditEnable = cardCreditEnable,
                    cardCreditUse = cardCreditUse
                )
            } else {
                //if the GL's RTP is higher then validates if the card credit is enabled on the GL
                greenListCreditEnable == 1
            }*/
            return true
        } catch (e: Exception) {
            Log.e("isCreditOfflineValidException", e.stackTraceToString())
            return false
        }
    }

    /** validates if the card is expired, if true post payment error **/
    private fun isCardExpired(): Boolean {
        val block13: String = CardBlocks.block13
        val mockExpirationDate = CreditPaymentTest.modifyCardExpirationDate(
            block13 = block13,
            info = "08A4", //2212
            cardInformation = cardExpirationDateInformation
        )
        val cardExpirationDate: String =
            getCardExpirationDate(
                cardInformation = cardExpirationDateInformation,
                block13 = CardBlocks.block13
//                block13 = mockExpirationDate
            )
        val isCardExpired: Boolean = compareCardExpirationDateWithCurrentDate(cardExpirationDate)
        Log.i("validateCardExpired", "cardExpirationDate: $cardExpirationDate")
        Log.i("validateCardExpired", "isCardExpired: $isCardExpired")
        return isCardExpired
    }

    /** Creates and posts a 'Card expired' payment response **/
    private fun cardExpiredPaymentResponse() {
        val paymentResponse: MakeSaleResponse = PaymentResponseBuilder
            .buildWarningPaymentResponse(
                code = Constants.CARD_PAYMENT_CARD_EXPIRED_CODE,
                message = Constants.CARD_PAYMENT_CARD_EXPIRED_MESSAGE,
                status = Constants.TRANSACTION_WARNING
            )
        _cardPaymentResponse.postValue(paymentResponse)
    }

    /** validate if the card is active, if it's not active, returns a not
    active card payment response **/
    private fun isCardActive(): Boolean {
        val block13 = CardBlocks.block13
        val mockCardStatus = CreditPaymentTest.modifyCardStatus(
            block13 = block13,
            info = "00", //00 desactivada
            cardInformation = cardStatusInformation
        )
        val cardStatus: String = getCardStatusUseCase(
            cardInformation = cardStatusInformation,
            block13 = CardBlocks.block13
//            block13 = mockCardStatus
        )
        val isCardStatusActive: Boolean = validateCardStatus(cardStatus)
        Log.i("validateCardActive", "isCardActive: $isCardStatusActive")
        return isCardStatusActive
    }

    /** Creates and posts a 'Card not active' payment response **/
    private fun cardDeactivatedPaymentResponse() {
        val paymentResponse: MakeSaleResponse = PaymentResponseBuilder
            .buildWarningPaymentResponse(
                code = Constants.CARD_PAYMENT_STATUS_CODE,
                message = Constants.CARD_PAYMENT_STATUS_DEACTIVATED_MESSAGE,
                status = Constants.TRANSACTION_WARNING
            )
        _cardPaymentResponse.postValue(paymentResponse)
    }

    private fun cardWithoutProductPaymentResponse() {
        val cardWithoutProductResponse = PaymentResponseBuilder
            .buildWarningPaymentResponse(
                code = Constants.CARD_WITHOUT_PRODUCT_CODE,
                message = Constants.CARD_WITHOUT_PRODUCT_MESSAGE,
                status = Constants.TRANSACTION_WARNING
            )
        _cardPaymentResponse.postValue(cardWithoutProductResponse)
    }

    /** flow that verifies if the card is being read by checking
     *  DeviceInformation.isCardBeingRead attribute **/
    fun isCardBeingReadFlow() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                nfcRepository.isCardBeingReadFlow.collect {
                    if (it) {
                        _isCardBeingReadLD.postValue(DeviceInformation.isCardBeingRead)
                    }
                }
            } catch (e: Exception) {
                Log.e("isCardBeingReadFlowException", e.stackTraceToString())
            }
        }
    }

    /** Read card method with nxp library **/
    fun readCard(intent: Intent) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cardBlocks: CardBlocksModel = readCardBlocksUseCase(intent)
                val block12: String = cardBlocks.block12
                val block13: String = cardBlocks.block13
                val block14: String = cardBlocks.block14
                val cardUID: String = getCardUidUseCase(cardUidInformation, block14)
                if (cardUID.isNotEmpty() && !TransactionStatus.isTransactionInProgress) {
                    _paymentCardUID.postValue(cardUID)
                }
            } catch (e: Exception) {
                Log.e("makeSinglePaymentException", e.stackTraceToString())
            }
        }
    }

    /** Process a card payment validating if it's either online or offline **/
    fun makeCardPayment(cardPaymentRequestBody: MakeSaleRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
//                val cardProfile: String = getCardProfile(
//                    cardInformation = cardProfileInformation,
//                    block12 = CardBlocks.block12
//                )

                //mock card profile 02
                val cardProfile: String = "02"

                //Get card block information
                val cardBalance = getCardBalanceUseCase(
                    cardInformation = cardBalanceInformation,
                    block12 = CardBlocks.block12
                )
                val cardRtp = getCardRtpUseCase(
                    cardInformation = cardRtpInformation,
                    block12 = CardBlocks.block12
                )
                val cardUID = getCardUidUseCase(
                    cardInformation = cardUidInformation,
                    block14 = CardBlocks.block14
                )

                val cardCreditUse: String = getCardCreditUsedUseCase(
                    cardInformation = cardCreditUseInformation,
                    block12 = CardBlocks.block12
                )

                val cardCreditEnable: String = getCardCreditEnableUseCase(
                    cardInformation = cardCreditEnableInformation,
                    block12 = CardBlocks.block12
                )

                val cardTranshipment: String = getCardTranshipmentUseCase(
                    cardInformation = cardTranshipmentInformation, block13 = CardBlocks.block13
                )

                Log.i("validateBlocks", "block12: ${CardBlocks.block12}")
                Log.i("validateBlocks", "block13: ${CardBlocks.block13}")
                Log.i("validateBlocks", "block14: ${CardBlocks.block14}")
                val cardSerialNumber: String = cardUID.toLong(radix = 16).toString()
                //val greenListItem: GreenList = getGreenListItemUseCase(cardSerialNumber)

                /** 3) evaluate rate by config **/
                val cardFare: Fares? = getCardFaresByProfile(cardProfile)
                Log.i("validateFare", "cardFare de $cardProfile: $cardFare")

                //validate if there's a product for the card profile
                if (cardFare == null) {
                    //if no product for the profile, post warning screen
                    cardWithoutProductPaymentResponse()
                    return@launch
                }

                //val shortGreenListRtp: String = RtpUtils.convertLongToShortRTP(greenListItem.rtp)

                /** 5) use most updated balance **/
                //get the balance from the most updated source (card or GL)
                /*val mostUpdatedBalance: Int = if (shortGreenListRtp.toLong() >= cardRtp.toLong()) {
                    greenListItem.balance
                } else {*/
                val mostUpdatedBalance: Int = cardBalance.toInt()
                //}
                Log.i("validateMostUpdatedBalance", "cardRtp $cardRtp")
                //Log.i("validateMostUpdatedBalance", "shortGreenListRtp $shortGreenListRtp")
                Log.i("validateMostUpdatedBalance", "cardBalance $cardBalance")
                /*Log.i(
                    "validateMostUpdatedBalance",
                    "greenListItem.balance ${greenListItem.balance}"
                )*/
                Log.i("validateMostUpdatedBalance", "mostUpdatedBalance $mostUpdatedBalance")

                //Setting up payment body information
                cardPaymentRequestBody.rec.balance = mostUpdatedBalance.toString()
                cardPaymentRequestBody.rec.amount = cardFare.value
                cardPaymentRequestBody.rec.idFare = cardFare.id.toString()
                cardPaymentRequestBody.rec.productId = cardFare.productId.toString()
                cardPaymentRequestBody.creditEnable = cardCreditEnable
                cardPaymentRequestBody.creditUse = cardCreditUse
                cardPaymentRequestBody.date = RtpUtils.getCurrentDateYYMMDDHHMM()
                cardPaymentRequestBody.transfer = when(cardTranshipment) {
                    "1" -> true
                    "0" -> false
                    else -> false
                }

                //Saving cardPaymentRequestBody to use along the transaction
                PaymentTransaction.setPaymentTransaction(cardPaymentRequestBody)

                //setting payment rate and getting card balance
                var paymentRateInt = cardPaymentRequestBody.rec.amount.toDouble()

                //Mock card balance
                val cardBalanceInt: Int = mostUpdatedBalance
//                val cardBalanceInt: Int = 0

                Log.i("validatePaymentRate", "cardFare.value: $paymentRateInt")

                /** 1) expiration date **/
                if (isCardExpired()) { //if card is expired
                    //Respond with card payment expired
                    cardExpiredPaymentResponse()
                    /** 2) card status **/
                } else if (!isCardActive()) { //if card is not active
                    //Respond with card not active
                    cardDeactivatedPaymentResponse()
                } else {
                    /** 4) evaluate transhipment **/
                    //validate if it's a transhipment and it's valid
                    val isTranshipmentRouteAndTimeValid: Boolean =
                        isTranshipmentRouteAndTimeValidByCardProfile(
                            cardProfile = cardProfile,
                            cardRtp = cardRtp,
                            //greenListItem = greenListItem
                        )

                    //if it's transhipment and it's valid, set the transhipment's rate as the payment rate
                    if (isTranshipmentRouteAndTimeValid) {
                        paymentRateInt = getTranshipmentPaymentRate(cardFare)
                        cardPaymentRequestBody.rec.amount = "$paymentRateInt"
                        Log.i("validatePaymentRate", "transhipment rate: $paymentRateInt")
                    }

                    /** 6) evaluate card balance is enough for payment **/
                    //if the card balance is not enough for the payment
                    // payment rate is regular or transhipment rate (previously validated)
                    if (cardBalanceInt <= paymentRateInt) {
                        processCreditPaymentIfValid(
                            cardPaymentRequestBody = cardPaymentRequestBody,
                            cardRtp = cardRtp.toLong(),
                            //greenListItem = greenListItem
                        )
                    } else { //else the card has enough balance to process regular payment
                        when (!DeviceInformation.isTransactionsOffline) {
                            false -> makeOnlinePayment(cardPaymentRequestBody)
                            true -> makeOnlinePayment(cardPaymentRequestBody)
                        }
                    }
                }
            } catch (e: Exception) {
                DeviceInformation.isCardBeingRead = false
                Log.e("makeCardPaymentException", e.stackTraceToString())
            }
        }
    }

    /** Process an online card payment **/
    private suspend fun makeOnlinePayment(cardPaymentRequestBody: MakeSaleRequest) {
        try {
            when (val cardPaymentResult: NetworkResult<MakeSaleResponseModel> =
                onlineCardPaymentUseCase(cardPaymentRequestBody)
            ) {
                is NetworkResult.ApiSuccess -> {
                    _cardPaymentResponse.postValue(cardPaymentResult.data.toDomain())
                }

                is NetworkResult.ApiError -> {
                    DeviceInformation.isCardBeingRead = false
                    Log.e(
                        "cardPaymentError",
                        cardPaymentResult.message
                    )
                }

                is NetworkResult.ApiException -> {
                    DeviceInformation.isCardBeingRead = false
                    Log.e(
                        "makeOnlinePaymentException",
                        cardPaymentResult.e.stackTraceToString()
                    )
                }
            }
        } catch (e: Exception) {
            DeviceInformation.isCardBeingRead = false
            Log.e("makeOnlinePaymentException", e.stackTraceToString())
        }
    }
}