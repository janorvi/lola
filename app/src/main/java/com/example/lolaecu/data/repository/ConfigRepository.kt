package com.example.lolaecu.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ConfigRepository @Inject constructor(

) {

    /** Get Config Flow **/
    val getConfigFlow: Flow<Boolean> = flow {
        while (true) {
            emit(true)
            delay(60000L)
        }
    }.flowOn(Dispatchers.IO)

    /** Update Time to display Flow **/
    val updateTimeToDisplay: Flow<Boolean> = flow<Boolean> {
        while (true) {
            emit(true)
            delay(1000L)
        }
    }.flowOn(Dispatchers.IO)
}