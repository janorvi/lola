package com.example.lolaecu.domain.useCases.frames

import com.example.lolaecu.core.utils.Constants
import com.example.lolaecu.core.utils.DeviceInformation
import com.example.lolaecu.data.repository.FramesRepository
import javax.inject.Inject

class F20FramesUseCase @Inject constructor(
    private val framesRepository: FramesRepository,
) {
    private val deviceModel = DeviceInformation.deviceModel
    suspend operator fun invoke(frameType: String) {

        when(deviceModel) {
            Constants.TELPO_T20_DEVICE_MODEL -> framesRepository.sendSelinaFrame(frameType)
            Constants.TELPO_TPS530_DEVICE_MODEL -> framesRepository.sendSelinaFrame(frameType)
            Constants.TELPO_TPS320_DEVICE_MODEL -> framesRepository.sendSelinaFrame(frameType)
            else -> framesRepository.sendSelinaFrame(frameType)
        }
    }
}