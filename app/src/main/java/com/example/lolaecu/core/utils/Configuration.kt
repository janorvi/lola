package com.example.lolaecu.core.utils

import com.example.lolaecu.data.model.ConfigResponseModel

object Configuration {

    private var validatorConfiguration: ConfigResponseModel = ConfigResponseModel()


    fun getConfiguration(): ConfigResponseModel {
        return this.validatorConfiguration
    }

    fun setConfiguration(configuration: ConfigResponseModel) {
        this.validatorConfiguration = configuration
    }
}