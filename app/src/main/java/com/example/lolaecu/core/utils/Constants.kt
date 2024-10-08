package com.example.lolaecu.core.utils

object Constants {


    //Selina IDSAVE
    const val idSave = 1

    //Prefs variable names /////////////////////////////////////////////////////
    //routeName
    const val routeName = "route_name"
    const val routeId = "routeId"
    const val driverId = "id"
    const val LOCATION_MODE = "isSimulation"

    const val MDT_IMEI = "mdtImei"
    ////////////////////////////////////////////////////////////////////////////


    //simulation route info
    const val ROUTE_NAME = "C9"
    const val ROUTE_CASE = "Caso1"
    //Simulation row index
    const val SIMULATION_ROUTE_INDEX = "current_row_Index"

    //Service
    const val ASSIGNED_SERVICE = "assigned_service"

    //API
    const val NXP_LIBRARY_KEY = "f4a19a1cd0dc00693ea4da785cef71f9"
//    private const val LOLA_URL_IP = "api-validator-v1.valliu.co"

    private const val LOLA_URL_IP = "quito.valliu.co"
    const val LOLA_API_URL = "https://$LOLA_URL_IP/api/"

    const val API_TOKEN = "FOSnfNmi7AdqdJq2NOOc3rvDsFrWrHwkF4HSthaK"

    //Bluetooth
    const val READER_MAC_ADDRESS_CAMILO = "54:81:2D:7E:0B:98"
    const val READER_MAC_ADDRESS_ANDRES = "54:81:2D:7E:0B:83"

    //KINPOS LIBRARY
    const val CUSTOMER_NAME = "KINPOS"
    const val VECTOR =
        "7FDC5D1EFE92EBB3B89B0307158F2C29A264EBB4599242E96F27A60801F0EF442050C1E1CCA9DCAFA3B098FE5381380C055D3A13E603B63318A8088825D595B9"
    const val DEFAULT_CARD_KEY = "23DE67D3456790AC3467DE2845FACE54"

    //Payment transaction types
    const val CARD_PAYMENT_TYPE = "RC111"
    const val QR_PAYMENT_TYPE = "RC110"
    const val CASH_PAYMENT_TYPE = "RC101"

    //Payment methods
    const val CARD_PAYMENT_METHOD = "Card"
    const val QR_PAYMENT_METHOD = "QR"
    const val CASH_PAYMENT_METHOD = "CASH"

    //Transaction status
    const val TRANSACTION_SUCCESS = "success"
    const val TRANSACTION_WARNING = "warning"
    const val TRANSACTION_DANGER = "danger"
    const val TRANSACTION_INFO = "info"

    //Device constants
    const val TELPO_TPS530_DEVICE_MODEL = "TPS530"
    const val TELPO_T20_DEVICE_MODEL = "T20"
    const val TELPO_TPS320_DEVICE_MODEL = "TPS320"

    //Payment responses

    const val CARD_WITHOUT_PRODUCT_CODE = "721"
    const val CARD_WITHOUT_PRODUCT_MESSAGE = "Tarjeta sin producto"

    const val CARD_PAYMENT_UNKNOWN_CARD_CODE = "713"
    const val CARD_PAYMENT_UNKNOWN_CARD_MESSAGE = "Tarjeta Desconocida"

    const val CARD_PAYMENT_SUCCESS_TRANSACTION_CODE = "711"
    const val CARD_PAYMENT_SUCCESS_TRANSACTION_MESSAGE = "Transaccion Exitosa Tarjeta"

    const val CARD_PAYMENT_NO_BALANCE_CODE = "712"
    const val CARD_PAYMENT_NO_BALANCE_MESSAGE = "Tarjeta Sin Saldo"

    const val CARD_PAYMENT_CARD_EXPIRED_CODE = "717"
    const val CARD_PAYMENT_CARD_EXPIRED_MESSAGE = "Tarjeta Vencida"

    const val CARD_PAYMENT_CREDIT_USED_CODE = "712"
    const val CARD_PAYMENT_CREDIT_USED_MESSAGE = "Credito agotado"

    const val CARD_PAYMENT_STATUS_CODE = "726"
    const val CARD_PAYMENT_STATUS_DEACTIVATED_MESSAGE = "Tarjeta desactivada"

    //Tourniquet commands
    const val OPEN_TOURNIQUET_COMMAND: String = "100203031003"
    const val TOURNIQUET_ABLE_TO_OPEN = "0D20011002035230000061100321"
    const val TOURNIQUET_TURNED_COMPLETELY = "0B2002100202060105100321"

    const val CARD_AUTHENTICATION_BLOCK: String = "0040"


    // ******************* Cards ********************************8

    //Block 12

    //balance
    const val CARD_BALANCE = "card_balance"
    const val CARD_BALANCE_BLOCK_NUMBER: String = "0C00" //block 12
    val CARD_BALANCE_INDEXES = Pair(2, 10)
    const val CARD_BALANCE_FIELD_SIZE = 8 //bits

    //Profile
    const val CARD_PROFILE = "card_profile"
    const val CARD_PROFILE_BLOCK_NUMBER = "0C00" // block 12
    val CARD_PROFILE_INDEXES = Pair(10,12)
    const val CARD_PROFILE_FIELD_SIZE = 2 //bits

    //Credit Enable
    const val CARD_CREDIT_ENABLE = "card_credit_enable"
    const val CARD_CREDIT_ENABLE_BLOCK_NUMBER: String = "0C00" // block 12
    val CARD_CREDIT_ENABLE_INDEXES = Pair(12,14)
    const val CARD_CREDIT_ENABLE_FIELD_SIZE = 2 //bits

    //Credit Travel use
    const val CARD_CREDIT_USE = "card_credit_use"
    const val CARD_CREDIT_USE_BLOCK_NUMBER = "0C00" // block 12
    val CARD_CREDIT_USE_INDEXES = Pair(14,16)
    const val CARD_CREDIT_USE_FIELD_SIZE = 2

    //Last Validator Serial
    const val CARD_LAST_VALIDATOR_SERIAL = "card_last_validator_serial"
    const val CARD_LAST_VALIDATOR_SERIAL_BLOCK_NUMBER = "0C00" //block 12
    val CARD_LAST_VALIDATOR_SERIAL_INDEXES = Pair(16,24)
    const val CARD_LAST_VALIDATOR_SERIAL_FIELD_SIZE = 8 //bits

    //RTP
    const val CARD_RTP = "card_rtp"
    const val CARD_RTP_BLOCK_NUMBER: String = "0C00" //block 12
    val CARD_RTP_INDEXES = Pair(24, 34)
    const val CARD_RTP_FIELD_SIZE = 10 //bits


    //Block 13

    //Transhipment
    const val CARD_TRANSHIPMENT = "card_transhipment"
    const val CARD_TRANSHIPMENT_BLOCK_NUMBER = "0D00" //block 13
    val CARD_TRANSHIPMENT_INDEXES = Pair(2,4)
    const val CARD_TRANSHIPMENT_FIELD_SIZE = 2 //bits

    //transhipment time
    const val CARD_TRANSHIPMENT_TIME = "card_transhipment_time"
    const val CARD_TRANSHIPMENT_TIME_BLOCK_NUMBER = "0D00" // block 13
    val CARD_TRANSHIPMENT_TIME_INDEXES = Pair(4,9)
    const val CARD_TRANSHIPMENT_TIME_FIELD_SIZE = 4 // bits

    //Expiration date
    const val CARD_EXPIRATION_DATE = "card_expiration_date"
    const val CARD_EXPIRATION_DATE_BLOCK_NUMBER = "0D00" // block 13
    val CARD_EXPIRATION_DATE_INDEXES = Pair(18,22)
    const val CARD_EXPIRATION_DATE_FIELD_SIZE = 4 //bits

    //Card status
    const val CARD_STATUS = "card_status"
    const val CARD_STATUS_BLOCK_NUMBER = "0D00" //block 13
    val CARD_STATUS_INDEXES = Pair(32,34)
    const val CARD_STATUS_FIELD_SIZE = 2


    //Block 14

    //uid
    const val CARD_UID = "card_uid"
    const val CARD_UID_BLOCK_NUMBER: String = "0E00" //block 14
    val CARD_UID_INDEXES = Pair(2, 16)
    const val CARD_UID_FIELD_SIZE = 14 //bits

    //Card last route validated
    const val CARD_LAST_ROUTE_VALIDATED_BLOCK_NUMBER = "0E00" //block14
    val CARD_LAST_ROUTE_VALIDATED_INDEXES = Pair(30,32)
    const val CARD_LAST_ROUTE_VALIDATED_FIELD_SIZE = 2 //bits

    //Card blocks
    const val CARD_BLOCK_12: String = "0C00"
    const val CARD_BLOCK_13: String = "0D00"
    const val CARD_BLOCK_14: String = "0E00"

    //Reader type
    const val NFC_READER_TYPE: String = "INTERNAL"
    const val NFC_READER_TYPE_INTERNAL: String = "INTERNAL"
    const val NFC_READER_TYPE_EXTERNAL: String = "EXTERNAL"

    //Routes codes
    const val ROUTE_100_NAME = "R100"
    const val ROUTE_100_CODE = 10

    const val ROUTE_101_NAME = "R101"
    const val ROUTE_101_CODE = 11

    const val ROUTE_214_NAME = "R214"
    const val ROUTE_214_CODE = 12

    const val ROUTE_313_NAME = "R313"
    const val ROUTE_313_CODE = 13

    const val ROUTE_316_NAME = "R316"
    const val ROUTE_316_CODE = 14

    const val ROUTE_412_NAME = "R412"
    const val ROUTE_412_CODE = 16

    const val ROUTE_515_NAME = "R515"
    const val ROUTE_515_CODE = 17

    const val ROUTE_561_NAME = "R561"
    const val ROUTE_561_CODE = 18

    const val ROUTE_562_NAME = "R562"
    const val ROUTE_562_CODE = 19





}

