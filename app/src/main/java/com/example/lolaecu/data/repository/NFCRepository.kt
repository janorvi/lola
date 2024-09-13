package com.example.lolaecu.data.repository

import android.content.Context
import android.util.Log
import com.example.lolaecu.core.utils.CardBlocks
import com.example.lolaecu.core.utils.Constants
import com.example.lolaecu.core.utils.DeviceInformation
import com.example.lolaecu.core.utils.HexUtil
import com.example.lolaecu.data.model.CardBlocksModel
import com.telpo.tps550.api.TelpoException
import com.telpo.tps550.api.TimeoutException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.internal.toHexString
import javax.inject.Inject

class NFCRepository @Inject constructor(
    private val nfcManagerTPS530: com.telpo.tps550.api.nfc.Nfc,
    @ApplicationContext private val context: Context
) {

    val readMifareNFCFlow: Flow<Boolean> = flow {
        while (true) {
            emit(true)
            delay(1000L)
        }
    }.flowOn(Dispatchers.IO)

    val isCardBeingReadFlow: Flow<Boolean> = flow<Boolean> {
        while (true) {
            emit(true)
            delay(1000)
        }
    }.flowOn(Dispatchers.IO)

    @Throws(Exception::class)
    @OptIn(ExperimentalUnsignedTypes::class)
    fun readCardTPS530(
    ): CardBlocksModel {
        return try {

            var cardInformationToReturn = CardBlocksModel()

            nfcManagerTPS530.open()
            nfcManagerTPS530.activate(2000)

            //if the validator is connected to the Card reader then try to
            //read a card and authenticate
            //if (posConnector.isConnected() && !DeviceInformation.isCardBeingRead) {
            if (!DeviceInformation.isCardBeingRead) {
//                posConnector.beep()
                //val cardKey: ByteArray =
                    //HexUtil.hexStringToByteArray(Constants.DEFAULT_CARD_KEY)

                //Detect card
                //val isCardDetected: Int = posConnector.detectMifareSl3()

                //Authenticate the card
                //val isCardAuthenticated: Int = posConnector.authMifareSl3(
                    //HexUtil.hexStringToByteArray(Constants.CARD_AUTHENTICATION_BLOCK),
                    //cardKey
                //)
                //if (isCardDetected == 0 && isCardAuthenticated == 0) {

                    //Set the is card being read = true so the "reading card" screen is shown
                    DeviceInformation.isCardBeingRead = true

                    //Get block 12
                    nfcManagerTPS530.m1_authenticate(HexUtil.hexStringToByteArray(Constants.CARD_BLOCK_12)[0], 0x0A, byteArrayOf(0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte()))
                    //val s = nfcManagerTPS530.m1_read_value(HexUtil.hexStringToByteArray(Constants.CARD_BLOCK_12)[0])
                    val block12ByteArray: ByteArray? = nfcManagerTPS530.m1_read_block(HexUtil.hexStringToByteArray(Constants.CARD_BLOCK_12)[0])

                    //Get block 13
                    nfcManagerTPS530.m1_authenticate(HexUtil.hexStringToByteArray(Constants.CARD_BLOCK_13)[0], 0x0A, byteArrayOf(0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte()))
                    val block13ByteArray: ByteArray? = nfcManagerTPS530.m1_read_block(HexUtil.hexStringToByteArray(Constants.CARD_BLOCK_13)[0])

                    //Get block 14
                    nfcManagerTPS530.m1_authenticate(HexUtil.hexStringToByteArray(Constants.CARD_BLOCK_14)[0], 0x0A, byteArrayOf(0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte()))
                    val block14ByteArray: ByteArray? = nfcManagerTPS530.m1_read_block(HexUtil.hexStringToByteArray(Constants.CARD_BLOCK_14)[0])

                    val cardBlock12: String = "00" + HexUtil.byteArrayToHexString(block12ByteArray!!).ifBlank { "" }
                    val cardBlock13: String = "00" + HexUtil.byteArrayToHexString(block13ByteArray!!).ifBlank { "" }
                    val cardBlock14: String = "00" + HexUtil.byteArrayToHexString(block14ByteArray!!).ifBlank { "" }

                    //Save blocks to use on the app
                    CardBlocks.block12 = cardBlock12
                    CardBlocks.block13 = cardBlock13
                    CardBlocks.block14 = cardBlock14

                    cardInformationToReturn = CardBlocksModel(
                        block12 = cardBlock12,
                        block13 = cardBlock13,
                        block14 = cardBlock14
                    )
                    Log.i("readCardD135", "cardInformationToReturn: $cardInformationToReturn")
                //}
            }
            nfcManagerTPS530.close()
            //DeviceInformation.isCardBeingRead = false
            cardInformationToReturn
        } catch (e: TimeoutException) {
            nfcManagerTPS530.close()
            Log.e("readMifareNFCException", e.stackTraceToString())
            DeviceInformation.isCardBeingRead = false
            CardBlocksModel()
        } catch (e: TelpoException){
            nfcManagerTPS530.m1_authenticate(0x00, 0x0A, byteArrayOf(0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte()))
            val info = nfcManagerTPS530.m1_read_block(0x00)
            val bloque00String: String = HexUtil.byteArrayToHexString(info)
            nfcManagerTPS530.m1_authenticate(HexUtil.hexStringToByteArray(Constants.CARD_BLOCK_12)[0], 0x0A, byteArrayOf(0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte()))
            nfcManagerTPS530.m1_write_block(HexUtil.hexStringToByteArray(Constants.CARD_BLOCK_12)[0], HexUtil.hexStringToByteArray("000012C002010100BC614E3593FD8B1B"),16)
            nfcManagerTPS530.m1_authenticate(HexUtil.hexStringToByteArray(Constants.CARD_BLOCK_13)[0], 0x0A, byteArrayOf(0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte()))
            nfcManagerTPS530.m1_write_block(HexUtil.hexStringToByteArray(Constants.CARD_BLOCK_13)[0], HexUtil.hexStringToByteArray("000000000542999E26B8000001010101"), 16)
            val bloque12String = "00" + bloque00String.substring(4) + "718000012E492B00000001"
            nfcManagerTPS530.m1_authenticate(HexUtil.hexStringToByteArray(Constants.CARD_BLOCK_14)[0], 0x0A, byteArrayOf(0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte()))
            nfcManagerTPS530.m1_write_block(HexUtil.hexStringToByteArray(Constants.CARD_BLOCK_14)[0], HexUtil.hexStringToByteArray(bloque12String),16)
            CardBlocksModel()
        }
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    fun writeCardTPS530(
        informationToWrite: String,
        blockNumber: String,
        fieldIndexes: Pair<Int, Int>,
        fieldSize: Int
    ) {
        try {
            //if (nfcManagerTPS530.) {
                //val cardKey: ByteArray =
                    //HexUtil.hexStringToByteArray(Constants.DEFAULT_CARD_KEY)
                //Detects card
                //val isCardDetected: Int = posConnector.detectMifareSl3()

                //Authenticate the card
                //val isCardAuthenticated: Int = posConnector.authMifareSl3(
                    //HexUtil.hexStringToByteArray(Constants.CARD_AUTHENTICATION_BLOCK),
                    //cardKey
                //)

                nfcManagerTPS530.m1_authenticate(
                    HexUtil.hexStringToByteArray(blockNumber)[0],
                    0x0A,
                    byteArrayOf(0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte())
                )

                //if (isCardDetected == 0 && isCardAuthenticated == 0) {
                    val cardBlockString: String = when (blockNumber) {
                        Constants.CARD_BLOCK_12 -> CardBlocks.block12
                        Constants.CARD_BLOCK_13 -> CardBlocks.block13
                        Constants.CARD_BLOCK_14 -> CardBlocks.block14
                        else -> ""
                    }

                    val informationToWriteHex: String =
                        informationToWrite
                            .toLong()
                            .toHexString()
                            .uppercase()

                    val remainingSize: Int = fieldSize - informationToWriteHex.length
                    var informationToWriteWithCorrectSize: String = ""

                    Log.i("writeCardTPS530", "//////////////////////////////////////////////")
                    Log.i("writeCardTPS530", "blockNumber: $blockNumber")
                    Log.i("writeCardTPS530", "informationToWrite: $informationToWrite")
                    Log.i("writeCardTPS530", "cardBlockString: $cardBlockString")
                    Log.i("writeCardTPS530", "informationToWriteHex: $informationToWriteHex")

                    if (remainingSize >= 0) {
                        informationToWriteWithCorrectSize =
                            "0".repeat(remainingSize) + informationToWriteHex
                    } else {
                        throw Exception("Information to save is bigger than the card field size")
                    }

                    val modifiedCardBlockString: String = cardBlockString
                        .replaceRange(
                            fieldIndexes.first,
                            fieldIndexes.second,
                            informationToWriteWithCorrectSize
                        )
                        .uppercase()

                    Log.i("writeCardTPS530", "modifiedCardBlockString: $modifiedCardBlockString")
                    Log.i(
                        "writeCardTPS530",
                        "informationToWriteWithCorrectSize: $informationToWriteWithCorrectSize"
                    )

                    nfcManagerTPS530.m1_write_block(
                        HexUtil.hexStringToByteArray(blockNumber)[0],
                        HexUtil.hexStringToByteArray(modifiedCardBlockString.substring(2)),
                        16
                    )
                //}
            //}
        } catch (e: Exception) {
            DeviceInformation.isCardBeingRead = false
            Log.e("writeCardTPS530Exception", e.stackTraceToString())
        }
    }
}