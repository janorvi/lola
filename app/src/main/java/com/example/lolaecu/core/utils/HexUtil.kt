package com.example.lolaecu.core.utils

object HexUtil {
    fun hexStringToByteArray(hexString: String): ByteArray {
        // Verifica que la longitud del string sea par
        require(hexString.length % 2 == 0) { "La longitud de la cadena hexadecimal debe ser par." }

        // Inicializa el arreglo de bytes
        val byteArray = ByteArray(hexString.length / 2)

        // Recorre la cadena hexadecimal en pares de caracteres
        for (i in byteArray.indices) {
            // Convierte cada par de caracteres hexadecimales en un byte
            val hexPair = hexString.substring(i * 2, i * 2 + 2)
            byteArray[i] = hexPair.toInt(16).toByte()
        }

        return byteArray
    }
    fun byteArrayToHexString(byteArray: ByteArray): String {
        // Crea un StringBuilder para construir la cadena hexadecimal
        val hexString = StringBuilder(byteArray.size * 2)

        // Recorre el arreglo de bytes
        for (byte in byteArray) {
            // Convierte cada byte a una cadena hexadecimal y añade al StringBuilder
            val hex = byte.toInt() and 0xFF // Convierte el byte a un valor entero sin signo
            hexString.append(String.format("%02x", hex))
        }

        return hexString.toString()
    }
}