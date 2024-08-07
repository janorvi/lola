package com.example.lolaecu.domain.useCases.services

import com.example.lolaecu.core.utils.Constants
import com.example.lolaecu.core.utils.DeviceInformation
import com.example.lolaecu.data.repository.ServicesRepository
import com.example.mdt.model.ServicePost
import javax.inject.Inject

class GetServicesUseCase @Inject constructor(
    private val servicesRepository: ServicesRepository
) {

    private val deviceModel = DeviceInformation.deviceModel

    operator fun invoke(getServicesRequestBody: ServicePost) {
        when (deviceModel) {
            Constants.TELPO_T20_DEVICE_MODEL -> servicesRepository.getServicesFromSAE(
                getServicesRequestBody
            )

            Constants.TELPO_TPS530_DEVICE_MODEL -> servicesRepository.getServicesFromSAE(
                getServicesRequestBody
            )

            Constants.TELPO_TPS320_DEVICE_MODEL -> servicesRepository.getServicesFromSAE(
                getServicesRequestBody
            )
        }
    }
}